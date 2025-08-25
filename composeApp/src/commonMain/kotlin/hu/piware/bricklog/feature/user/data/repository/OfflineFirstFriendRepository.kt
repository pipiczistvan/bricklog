@file:OptIn(ExperimentalCoroutinesApi::class)

package hu.piware.bricklog.feature.user.data.repository

import co.touchlab.kermit.Logger
import hu.piware.bricklog.feature.core.domain.AccountSyncedRepository
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.user.domain.datasource.LocalFriendDataSource
import hu.piware.bricklog.feature.user.domain.datasource.RemoteFriendDataSource
import hu.piware.bricklog.feature.user.domain.manager.SessionManager
import hu.piware.bricklog.feature.user.domain.manager.filterAuthenticated
import hu.piware.bricklog.feature.user.domain.manager.isAuthenticated
import hu.piware.bricklog.feature.user.domain.model.Friend
import hu.piware.bricklog.feature.user.domain.model.UserId
import hu.piware.bricklog.feature.user.domain.repository.FriendRepository
import hu.piware.bricklog.util.firstOrDefault
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import org.koin.core.annotation.Single

@Single
class OfflineFirstFriendRepository(
    private val localDataSource: LocalFriendDataSource,
    private val remoteDataSource: RemoteFriendDataSource,
    private val sessionManager: SessionManager,
) : FriendRepository, AccountSyncedRepository {

    private val logger = Logger.withTag("OfflineFirstFriendRepository")

    private var syncJob: Job? = null

    override fun startSync(scope: CoroutineScope) {
        syncJob?.cancel()
        syncJob = sessionManager.userId
            .filterAuthenticated()
            .syncRemoteFriends()
            .launchIn(scope)
    }

    override suspend fun clearLocalData(userId: UserId): EmptyResult<DataError> {
        return localDataSource.deleteUserFriends(userId)
    }

    override fun watchFriends(userId: UserId): Flow<List<Friend>> {
        return localDataSource.watchFriends(userId)
    }

    override suspend fun saveFriends(
        userId: UserId,
        friends: List<Friend>,
    ): EmptyResult<DataError> {
        return if (userId.isAuthenticated) {
            remoteDataSource.upsertFriends(userId, friends)
        } else {
            localDataSource.upsertFriends(userId, friends)
        }
    }

    override suspend fun deleteFriends(
        userId: UserId,
        friendIds: List<UserId>,
    ): EmptyResult<DataError> {
        return if (userId.isAuthenticated) {
            remoteDataSource.deleteFriends(userId, friendIds)
        } else {
            localDataSource.deleteFriends(userId, friendIds)
        }
    }

    private fun Flow<UserId>.syncRemoteFriends() =
        flatMapLatest { userId ->
            remoteDataSource.watchFriends(userId).map { Pair(userId, it) }
        }.onEach { (userId, remoteFriends) ->
            logger.d { "Syncing ${remoteFriends.size} friends for user $userId" }
            val localFriends = localDataSource.watchFriends(userId)
                .firstOrDefault { emptyList() }
            val friendsToDelete = localFriends.filter { localFriend ->
                remoteFriends.none { remoteFriend ->
                    remoteFriend.id == localFriend.id
                }
            }.map { it.id }
            val friendsToUpsert = remoteFriends.filter { remoteFriend ->
                localFriends.none { localFriend ->
                    localFriend == remoteFriend
                }
            }

            if (friendsToDelete.isNotEmpty()) {
                localDataSource.deleteFriends(userId, friendsToDelete)
            }
            if (friendsToUpsert.isNotEmpty()) {
                localDataSource.upsertFriends(userId, friendsToUpsert)
            }
            logger.d { "Deleted ${friendsToDelete.size} and upserted ${friendsToUpsert.size} friends for user $userId" }
        }.map { it.first }
}

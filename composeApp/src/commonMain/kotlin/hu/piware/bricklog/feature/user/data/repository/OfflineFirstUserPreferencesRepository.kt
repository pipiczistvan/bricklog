@file:OptIn(ExperimentalCoroutinesApi::class)

package hu.piware.bricklog.feature.user.data.repository

import co.touchlab.kermit.Logger
import hu.piware.bricklog.feature.core.domain.AccountSyncedRepository
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.core.domain.onError
import hu.piware.bricklog.feature.user.domain.datasource.LocalUserPreferencesDataSource
import hu.piware.bricklog.feature.user.domain.datasource.RemoteUserPreferencesDataSource
import hu.piware.bricklog.feature.user.domain.manager.SessionManager
import hu.piware.bricklog.feature.user.domain.manager.filterAuthenticated
import hu.piware.bricklog.feature.user.domain.model.UserId
import hu.piware.bricklog.feature.user.domain.model.UserPreferences
import hu.piware.bricklog.feature.user.domain.repository.UserPreferencesRepository
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
class OfflineFirstUserPreferencesRepository(
    private val localDataSource: LocalUserPreferencesDataSource,
    private val remoteDataSource: RemoteUserPreferencesDataSource,
    private val sessionManager: SessionManager,
) : UserPreferencesRepository, AccountSyncedRepository {

    private val logger = Logger.withTag("OfflineFirstUserPreferencesRepository")

    private var syncJob: Job? = null

    override fun startSync(scope: CoroutineScope) {
        syncJob?.cancel()
        syncJob = sessionManager.userId
            .filterAuthenticated()
            .flatMapLatest { userId ->
                remoteDataSource.watchUserPreferences(userId).map { Pair(userId, it) }
            }
            .syncRemoteUserPreferences()
            .launchIn(scope)
    }

    override suspend fun clearLocalData(): EmptyResult<DataError> {
        return localDataSource.deleteUserPreferences(sessionManager.currentUserId)
    }

    override fun watchUserPreferences(): Flow<UserPreferences> {
        return sessionManager.userId.flatMapLatest { userId ->
            localDataSource.watchUserPreferences(userId)
        }
    }

    override suspend fun saveUserPreferences(userPreferences: UserPreferences): EmptyResult<DataError> {
        localDataSource.upsertUserPreferences(sessionManager.currentUserId, userPreferences)
            .onError { return it }

        if (sessionManager.isAuthenticated) {
            remoteDataSource.upsertUserPreferences(sessionManager.currentUserId, userPreferences)
                .onError { return it }
        }

        return Result.Success(Unit)
    }

    private fun Flow<Pair<UserId, UserPreferences?>>.syncRemoteUserPreferences() =
        onEach { (userId, remoteUserPreferences) ->
            logger.d { "Syncing user preferences" }
            if (remoteUserPreferences == null) {
                localDataSource.deleteUserPreferences(userId)
            } else {
                localDataSource.upsertUserPreferences(userId, remoteUserPreferences)
            }
        }
}

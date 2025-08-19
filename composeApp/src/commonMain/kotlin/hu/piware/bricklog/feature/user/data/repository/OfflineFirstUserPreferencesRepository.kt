@file:OptIn(ExperimentalCoroutinesApi::class)

package hu.piware.bricklog.feature.user.data.repository

import co.touchlab.kermit.Logger
import hu.piware.bricklog.feature.core.domain.AccountSyncedRepository
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.user.domain.datasource.LocalUserPreferencesDataSource
import hu.piware.bricklog.feature.user.domain.datasource.RemoteUserPreferencesDataSource
import hu.piware.bricklog.feature.user.domain.manager.SessionManager
import hu.piware.bricklog.feature.user.domain.manager.filterAuthenticated
import hu.piware.bricklog.feature.user.domain.manager.isAuthenticated
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
            .syncRemoteUserPreferences()
            .launchIn(scope)
    }

    override suspend fun clearLocalData(userId: UserId): EmptyResult<DataError> {
        return localDataSource.deleteUserPreferences(userId)
    }

    override fun watchUserPreferences(userId: UserId): Flow<UserPreferences> {
        return localDataSource.watchUserPreferences(userId)
    }

    override suspend fun saveUserPreferences(
        userId: UserId,
        userPreferences: UserPreferences,
    ): EmptyResult<DataError> {
        return if (userId.isAuthenticated) {
            remoteDataSource.upsertUserPreferences(userId, userPreferences)
        } else {
            localDataSource.upsertUserPreferences(userId, userPreferences)
        }
    }

    private fun Flow<UserId>.syncRemoteUserPreferences() =
        flatMapLatest { userId ->
            remoteDataSource.watchUserPreferences(userId).map { Pair(userId, it) }
        }.onEach { (userId, remoteUserPreferences) ->
            logger.d { "Syncing user preferences" }
            if (remoteUserPreferences == null) {
                localDataSource.deleteUserPreferences(userId)
            } else {
                localDataSource.upsertUserPreferences(userId, remoteUserPreferences)
            }
        }.map { it.first }
}

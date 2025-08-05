package hu.piware.bricklog.feature.core.domain

import kotlinx.coroutines.CoroutineScope

interface AccountSyncedRepository {
    fun startSync(scope: CoroutineScope)
    suspend fun clearLocalData(): EmptyResult<DataError>
}

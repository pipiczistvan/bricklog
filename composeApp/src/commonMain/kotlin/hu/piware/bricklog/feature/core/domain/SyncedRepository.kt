package hu.piware.bricklog.feature.core.domain

import kotlinx.coroutines.CoroutineScope

interface SyncedRepository {
    fun startSync(scope: CoroutineScope)
    suspend fun clearLocal(): EmptyResult<DataError>
}

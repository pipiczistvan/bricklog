package hu.piware.bricklog.feature.core.domain

import hu.piware.bricklog.feature.user.domain.model.UserId
import kotlinx.coroutines.CoroutineScope

interface AccountSyncedRepository {
    fun startSync(scope: CoroutineScope)
    suspend fun clearLocalData(userId: UserId): EmptyResult<DataError>
}

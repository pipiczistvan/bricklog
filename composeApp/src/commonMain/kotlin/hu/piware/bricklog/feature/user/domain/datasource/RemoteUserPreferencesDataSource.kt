package hu.piware.bricklog.feature.user.domain.datasource

import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.user.domain.model.UserId
import hu.piware.bricklog.feature.user.domain.model.UserPreferences
import kotlinx.coroutines.flow.Flow

interface RemoteUserPreferencesDataSource {

    fun watchUserPreferences(userId: UserId): Flow<UserPreferences?>

    suspend fun upsertUserPreferences(
        userId: UserId,
        userPreferences: UserPreferences,
    ): EmptyResult<DataError.Remote>
}

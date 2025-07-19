package hu.piware.bricklog.feature.settings.domain.datasource

import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.settings.domain.model.UserPreferences
import kotlinx.coroutines.flow.Flow

interface RemoteSettingsDataSource {

    fun watchUserPreferences(userId: String): Flow<UserPreferences?>

    suspend fun saveUserPreferences(
        userId: String,
        userPreferences: UserPreferences,
    ): EmptyResult<DataError>
}

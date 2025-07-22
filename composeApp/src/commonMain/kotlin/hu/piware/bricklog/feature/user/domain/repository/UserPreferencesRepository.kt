package hu.piware.bricklog.feature.user.domain.repository

import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.user.domain.model.UserPreferences
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    fun watchUserPreferences(): Flow<UserPreferences>
    suspend fun saveUserPreferences(userPreferences: UserPreferences): EmptyResult<DataError>
}

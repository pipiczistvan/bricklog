package hu.piware.bricklog.mock

import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.settings.domain.datasource.RemoteSettingsDataSource
import hu.piware.bricklog.feature.settings.domain.model.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.update

class MockRemoteSettingsDataSource : RemoteSettingsDataSource {

    private val firestore = MockFirestore

    override fun watchUserPreferences(userId: String): Flow<UserPreferences?> {
        return firestore.userPreferences
    }

    override suspend fun saveUserPreferences(
        userId: String,
        userPreferences: UserPreferences,
    ): EmptyResult<DataError> {
        firestore.userPreferences.update {
            it
        }
        return Result.Success(Unit)
    }
}

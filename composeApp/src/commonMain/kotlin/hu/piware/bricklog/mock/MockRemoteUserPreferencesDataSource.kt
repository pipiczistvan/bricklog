package hu.piware.bricklog.mock

import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.user.domain.datasource.RemoteUserPreferencesDataSource
import hu.piware.bricklog.feature.user.domain.model.UserId
import hu.piware.bricklog.feature.user.domain.model.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.update

class MockRemoteUserPreferencesDataSource : RemoteUserPreferencesDataSource {

    private val firestore = MockFirestore

    override fun watchUserPreferences(userId: UserId): Flow<UserPreferences?> {
        return firestore.userPreferences
    }

    override suspend fun saveUserPreferences(
        userId: UserId,
        userPreferences: UserPreferences,
    ): EmptyResult<DataError.Remote> {
        firestore.userPreferences.update {
            it
        }
        return Result.Success(Unit)
    }
}

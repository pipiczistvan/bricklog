package hu.piware.bricklog.feature.user.data.database

import co.touchlab.kermit.Logger
import hu.piware.bricklog.feature.core.data.database.BricklogDatabase
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.user.domain.datasource.LocalUserPreferencesDataSource
import hu.piware.bricklog.feature.user.domain.manager.SessionManager
import hu.piware.bricklog.feature.user.domain.model.UserId
import hu.piware.bricklog.feature.user.domain.model.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single
class RoomUserPreferencesDataSource(
    database: BricklogDatabase,
) : LocalUserPreferencesDataSource {

    private val logger = Logger.withTag("RoomLocalUserPreferencesDataSource")

    private val userPreferencesDao = database.userPreferencesDao

    override fun watchUserPreferences(userId: UserId): Flow<UserPreferences> {
        return userPreferencesDao.watchUserPreferences(userId)
            .map { it?.toDomainModel() ?: SessionManager.GUEST_PREFERENCES }
    }

    override suspend fun upsertUserPreferences(
        userId: UserId,
        userPreferences: UserPreferences,
    ): EmptyResult<DataError.Local> {
        return try {
            logger.d { "Upserting user preferences $userPreferences" }
            userPreferencesDao.upsertUserPreferences(userPreferences.toEntity(userId))
            Result.Success(Unit)
        } catch (e: Exception) {
            logger.e(e) { "Error upserting user preferences $userPreferences" }
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun deleteUserPreferences(userId: UserId): EmptyResult<DataError.Local> {
        return try {
            logger.d { "Deleting user preferences" }
            userPreferencesDao.deleteUserPreferences(userId)
            Result.Success(Unit)
        } catch (e: Exception) {
            logger.e(e) { "Error deleting user preferences" }
            Result.Error(DataError.Local.UNKNOWN)
        }
    }
}

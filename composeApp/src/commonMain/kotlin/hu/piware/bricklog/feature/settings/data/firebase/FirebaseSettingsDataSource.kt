package hu.piware.bricklog.feature.settings.data.firebase

import co.touchlab.kermit.Logger
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.settings.domain.datasource.RemoteSettingsDataSource
import hu.piware.bricklog.feature.settings.domain.model.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Single

@Single
class FirebaseSettingsDataSource : RemoteSettingsDataSource {

    private val logger = Logger.Companion.withTag("FirebaseSettingsDataSource")

    private val firestore = Firebase.firestore

    override fun watchUserPreferences(userId: String): Flow<UserPreferences?> {
        return flow {
            try {
                firestore.document("user-data/$userId").snapshots.collect { snapshot ->
                    val preferences = snapshot.data<UserPreferencesDocument>().toDomainModel()
                    emit(preferences)
                }
            } catch (e: Exception) {
                logger.e(e) { "An error occurred while fetching user preferences" }
                emit(null)
            }
        }
    }

    override suspend fun saveUserPreferences(
        userId: String,
        userPreferences: UserPreferences,
    ): EmptyResult<DataError> {
        return try {
            firestore.document("user-data/$userId")
                .set(
                    data = userPreferences.toDocument(),
                    merge = true
                )

            Result.Success(Unit)
        } catch (e: Exception) {
            logger.e(e) { "An error occurred while saving user preferences" }
            Result.Error(DataError.Remote.UNKNOWN)
        }
    }
}

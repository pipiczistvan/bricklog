package hu.piware.bricklog.feature.user.data.firebase

import co.touchlab.kermit.Logger
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.settings.data.firebase.UserPreferencesDocument
import hu.piware.bricklog.feature.settings.data.firebase.toDocument
import hu.piware.bricklog.feature.settings.data.firebase.toDomainModel
import hu.piware.bricklog.feature.user.domain.datasource.RemoteUserPreferencesDataSource
import hu.piware.bricklog.feature.user.domain.model.UserId
import hu.piware.bricklog.feature.user.domain.model.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import org.koin.core.annotation.Single

@Single
class FirebaseUserPreferencesDataSource : RemoteUserPreferencesDataSource {

    private val logger = Logger.withTag("FirebaseUserPreferencesDataSource")

    private val firestore = Firebase.firestore

    override fun watchUserPreferences(userId: UserId): Flow<UserPreferences?> {
        return firestore.document("user-data/$userId").snapshots
            .mapNotNull { snapshot ->
                try {
                    snapshot.data<UserPreferencesDocument>().toDomainModel()
                } catch (e: Exception) {
                    logger.w(e) { "An error occurred while fetching user preferences" }
                    null
                }
            }
    }

    override suspend fun upsertUserPreferences(
        userId: UserId,
        userPreferences: UserPreferences,
    ): EmptyResult<DataError.Remote> {
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

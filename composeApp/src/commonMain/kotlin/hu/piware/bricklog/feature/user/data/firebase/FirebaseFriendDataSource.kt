package hu.piware.bricklog.feature.user.data.firebase

import co.touchlab.kermit.Logger
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.FirebaseFirestoreException
import dev.gitlive.firebase.firestore.firestore
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.user.domain.datasource.RemoteFriendDataSource
import hu.piware.bricklog.feature.user.domain.model.Friend
import hu.piware.bricklog.feature.user.domain.model.UserId
import hu.piware.bricklog.feature.user.domain.model.isNew
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import org.koin.core.annotation.Single

@Single
class FirebaseFriendDataSource : RemoteFriendDataSource {

    private val logger = Logger.withTag("FirebaseFriendDataSource")

    private val firestore by lazy { Firebase.firestore }

    override fun watchFriends(userId: UserId): Flow<List<Friend>> {
        return firestore
            .document("user-data/$userId")
            .collection("friends")
            .snapshots
            .mapNotNull { snapshot ->
                try {
                    snapshot.documents.map { document ->
                        document.data<FriendDocument>().toDomainModel(document.id)
                    }
                } catch (e: FirebaseFirestoreException) {
                    logger.w(e) { "An error occurred while fetching friends" }
                    null
                }
            }
    }

    override suspend fun upsertFriends(
        userId: UserId,
        friends: List<Friend>,
    ): EmptyResult<DataError.Remote> {
        return try {
            with(firestore) {
                batch().apply {
                    for (friend in friends) {
                        if (friend.isNew) {
                            document("user-data/$userId")
                                .collection("friends")
                                .add(friend.toDocument())
                            logger.d { "Friend added: ${friend.id} for user: $userId" }
                        } else {
                            document("user-data/$userId")
                                .collection("friends")
                                .document(friend.id)
                                .set(
                                    friend.toDocument(),
                                    merge = true,
                                )
                            logger.d { "Friend updated: ${friend.id} for user: $userId" }
                        }
                    }
                    commit()
                }
            }

            Result.Success(Unit)
        } catch (e: Exception) {
            logger.e(e) { "An error occurred while saving friends" }
            Result.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun deleteFriends(
        userId: UserId,
        friendIds: List<UserId>,
    ): EmptyResult<DataError.Remote> {
        return try {
            with(firestore) {
                batch().apply {
                    for (friendId in friendIds) {
                        delete(
                            documentRef = document("user-data/$userId")
                                .collection("friends")
                                .document(friendId),
                        )
                        logger.d { "Friend deleted: $friendId for user: $userId" }
                    }
                    commit()
                }
            }

            Result.Success(Unit)
        } catch (e: Exception) {
            logger.e(e) { "An error occurred while deleting friends" }
            Result.Error(DataError.Remote.UNKNOWN)
        }
    }
}

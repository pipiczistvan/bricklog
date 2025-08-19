package hu.piware.bricklog.feature.collection.data.firebase

import co.touchlab.kermit.Logger
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.FieldValue
import dev.gitlive.firebase.firestore.FirebaseFirestoreException
import dev.gitlive.firebase.firestore.firestore
import hu.piware.bricklog.feature.collection.domain.datasource.RemoteCollectionDataSource
import hu.piware.bricklog.feature.collection.domain.model.Collection
import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.collection.domain.model.isNew
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.set.domain.model.SetId
import hu.piware.bricklog.feature.user.domain.model.UserId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import org.koin.core.annotation.Single

@Single
class FirebaseCollectionDataSource : RemoteCollectionDataSource {

    private val logger = Logger.withTag("FirebaseCollectionDataSource")

    private val firestore by lazy { Firebase.firestore }

    override fun watchUserAndSharedCollections(userId: UserId): Flow<List<Collection>> {
        return firestore
            .collection("user-collections")
            .where { ("owner" equalTo userId) or ("sharedWith" contains userId) }
            .snapshots
            .mapNotNull { snapshot ->
                try {
                    snapshot.documents.map { document ->
                        document.data<CollectionDocument>().toDomainModel(document.id)
                    }
                } catch (e: FirebaseFirestoreException) {
                    logger.w(e) { "An error occurred while fetching collections" }
                    null
                }
            }
    }

    override fun watchUserAndSharedCollectionsBySets(userId: UserId): Flow<Map<SetId, List<CollectionId>>> {
        return firestore
            .collection("user-collections")
            .where { ("owner" equalTo userId) or ("sharedWith" contains userId) }
            .snapshots
            .mapNotNull { snapshot ->
                try {
                    snapshot.documents.flatMap { document ->
                        val setIds = document.get<List<SetId>?>("setIds")
                        setIds?.map { setId ->
                            setId to document.id
                        } ?: emptyList()
                    }.groupBy({ it.first }, { it.second })
                } catch (e: FirebaseFirestoreException) {
                    logger.w(e) { "An error occurred while fetching collections" }
                    null
                }
            }
    }

    override suspend fun upsertCollections(
        collections: List<Collection>,
    ): EmptyResult<DataError.Remote> {
        return try {
            with(firestore) {
                batch().apply {
                    for (collection in collections) {
                        if (collection.isNew) {
                            collection("user-collections")
                                .add(collection.toDocument())
                            logger.d { "Collection created successfully" }
                        } else {
                            collection("user-collections")
                                .document(collection.id)
                                .set(
                                    data = collection.toDocument(),
                                    merge = true,
                                )
                            logger.d { "Collection updated successfully" }
                        }
                    }
                    commit()
                }
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            logger.e(e) { "An error occurred while saving collection" }
            Result.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun addSetToCollections(
        setId: SetId,
        collectionIds: List<CollectionId>,
    ): EmptyResult<DataError.Remote> {
        return try {
            with(firestore) {
                batch().apply {
                    for (collectionId in collectionIds) {
                        set(
                            documentRef = collection("user-collections")
                                .document(collectionId),
                            data = mapOf(
                                "setIds" to FieldValue.arrayUnion(setId),
                            ),
                            merge = true,
                        )
                    }
                    commit()
                }
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            logger.e(e) { "An error occurred while adding set to collections" }
            Result.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun deleteCollections(
        collectionIds: List<CollectionId>,
    ): EmptyResult<DataError.Remote> {
        return try {
            with(firestore) {
                batch().apply {
                    for (id in collectionIds) {
                        delete(
                            documentRef = collection("user-collections")
                                .document(id),
                        )
                    }
                    commit()
                }
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            logger.e(e) { "An error occurred while deleting collection" }
            Result.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun removeSetFromCollections(
        setId: SetId,
        collectionIds: List<CollectionId>,
    ): EmptyResult<DataError.Remote> {
        return try {
            with(firestore) {
                batch().apply {
                    for (id in collectionIds) {
                        set(
                            documentRef = collection("user-collections")
                                .document(id),
                            data = mapOf(
                                "setIds" to FieldValue.arrayRemove(setId),
                            ),
                            merge = true,
                        )
                    }
                    commit()
                }
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            logger.e(e) { "An error occurred while removing set from collections" }
            Result.Error(DataError.Remote.UNKNOWN)
        }
    }
}

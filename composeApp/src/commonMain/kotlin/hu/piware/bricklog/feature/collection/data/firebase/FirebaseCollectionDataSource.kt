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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Single

@Single
class FirebaseCollectionDataSource : RemoteCollectionDataSource {

    private val logger = Logger.withTag("FirebaseCollectionDataSource")

    private val firestore = Firebase.firestore

    override fun watchCollections(userId: String): Flow<List<Collection>> {
        return flow {
            try {
                firestore.collection("user-data/$userId/collections").snapshots.collect { snapshot ->
                    val collections = snapshot.documents.map { document ->
                        document.data<CollectionDocument>().toDomainModel(document.id)
                    }
                    emit(collections)
                }
            } catch (e: FirebaseFirestoreException) {
                logger.e(e) { "An error occurred while fetching collections" }
                emit(emptyList())
            }
        }
    }

    override fun watchSetCollections(userId: String): Flow<Map<SetId, List<CollectionId>>> {
        return flow {
            try {
                firestore.collection("user-data/$userId/set-collections").snapshots.collect { snapshot ->
                    val setCollections = snapshot.documents.flatMap { document ->
                        val setIds = document.get<List<SetId>?>("setIds")
                        setIds?.map { setId ->
                            setId to document.id
                        } ?: emptyList()
                    }.groupBy({ it.first }, { it.second })
                    emit(setCollections)
                }
            } catch (e: FirebaseFirestoreException) {
                logger.e(e) { "An error occurred while fetching collections" }
                emit(emptyMap())
            }
        }
    }

    override suspend fun deleteCollection(
        userId: String,
        id: CollectionId,
    ): EmptyResult<DataError.Remote> {
        return try {
            with(firestore) {
                batch().apply {
                    delete(collection("user-data/$userId/set-collections").document(id))
                    delete(collection("user-data/$userId/collections").document(id))
                    commit()
                }
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            logger.e(e) { "An error occurred while deleting collection" }
            Result.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun upsertCollection(
        userId: String,
        collection: Collection,
    ): EmptyResult<DataError.Remote> {
        return try {
            if (collection.isNew) {
                firestore.collection("user-data/$userId/collections").add(collection.toDocument())
                logger.i { "Collection created successfully" }
            } else {
                firestore.collection("user-data/$userId/collections")
                    .document(collection.id)
                    .set(
                        data = collection.toDocument(),
                        merge = true
                    )
                logger.i { "Collection updated successfully" }
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            logger.e(e) { "An error occurred while saving collection" }
            Result.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun addSetToCollection(
        userId: String,
        setId: SetId,
        collectionId: CollectionId,
    ): EmptyResult<DataError.Remote> {
        return try {
            firestore.collection("user-data/$userId/set-collections")
                .document(collectionId)
                .set(
                    data = mapOf(
                        "setIds" to FieldValue.arrayUnion(setId)
                    ),
                    merge = true
                )
            Result.Success(Unit)
        } catch (e: Exception) {
            logger.e(e) { "An error occurred while adding set to collection" }
            Result.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun removeSetFromCollection(
        userId: String,
        setId: SetId,
        collectionId: CollectionId,
    ): EmptyResult<DataError.Remote> {
        return try {
            firestore.collection("user-data/$userId/set-collections")
                .document(collectionId)
                .set(
                    data = mapOf(
                        "setIds" to FieldValue.arrayRemove(setId)
                    ),
                    merge = true
                )
            Result.Success(Unit)
        } catch (e: Exception) {
            logger.e(e) { "An error occurred while removing set from collection" }
            Result.Error(DataError.Remote.UNKNOWN)
        }
    }
}

package hu.piware.bricklog.feature.set.data.firebase

import co.touchlab.kermit.Logger
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.FirebaseFirestoreException
import dev.gitlive.firebase.firestore.firestore
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.set.domain.datasource.RemoteDataServiceDataSource
import hu.piware.bricklog.feature.set.domain.model.BatchExportInfo
import hu.piware.bricklog.feature.set.domain.model.Collectible
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Single

@Single
class FirebaseDataServiceDataSource : RemoteDataServiceDataSource {

    private val logger = Logger.withTag("FirebaseDataServiceDataSource")

    private val firestore = Firebase.firestore

    override suspend fun getBatchExportInfo(): Result<BatchExportInfo, DataError> {
        try {
            val batchExportInfo = firestore
                .collection("bricklog-data-service")
                .document("batch-export-info")
                .get()
                .data<BatchExportInfoDocument>()
                .toDomainModel()

            return Result.Success(batchExportInfo)
        } catch (e: Exception) {
            return Result.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun getCollectibles(): Result<List<Collectible>, DataError> {
        try {
            val collectibles = firestore
                .collection("collectibles")
                .get()
                .documents
                .map {
                    it.data<CollectibleDocument>().toDomainModel(it.id)
                }

            return Result.Success(collectibles)
        } catch (e: Exception) {
            logger.e(e) { "An error occurred while fetching collectibles" }
            return Result.Error(DataError.Remote.UNKNOWN)
        }
    }

    override fun watchCollectibles(): Flow<List<Collectible>> {
        return flow {
            try {
                firestore.collection("collectibles").snapshots.collect { snapshot ->
                    val collectibles = snapshot.documents.map { document ->
                        document.data<CollectibleDocument>().toDomainModel(document.id)
                    }
                    emit(collectibles)
                }
            } catch (e: FirebaseFirestoreException) {
                logger.e(e) { "An error occurred while fetching collections" }
                emit(emptyList())
            }
        }
    }
}

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
import hu.piware.bricklog.feature.set.domain.model.ExportInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import org.koin.core.annotation.Single

@Single
class FirebaseDataServiceDataSource : RemoteDataServiceDataSource {

    private val logger = Logger.withTag("FirebaseDataServiceDataSource")

    private val firestore by lazy { Firebase.firestore }

    override suspend fun getBatchExportInfo(): Result<BatchExportInfo, DataError.Remote> {
        try {
            logger.d { "Fetching batch export info from Firestore" }
            val batchExportInfo = firestore
                .collection("bricklog-data-service")
                .document("batch-export-info")
                .get()
                .data<BatchExportInfoDocument>()
                .toDomainModel()

            return Result.Success(batchExportInfo)
        } catch (e: Exception) {
            logger.e(e) { "An error occurred while fetching batch export info" }
            return Result.Error(DataError.Remote.UNKNOWN)
        }
    }

    override fun watchCollectibles(): Flow<List<Collectible>> {
        return firestore.collection("collectibles").snapshots
            .mapNotNull { snapshot ->
                try {
                    snapshot.documents.map { document ->
                        document.data<CollectibleDocument>().toDomainModel(document.id)
                    }
                } catch (e: FirebaseFirestoreException) {
                    logger.w(e) { "An error occurred while fetching collections" }
                    null
                }
            }
    }

    override suspend fun getEurRateExportInfo(): Result<ExportInfo, DataError.Remote> {
        try {
            logger.d { "Fetching eur rate export info from Firestore" }
            val exportInfo = firestore
                .collection("bricklog-data-service")
                .document("eur-rate-export-info")
                .get()
                .data<ExportInfoDocument>()
                .toDomainModel()

            return Result.Success(exportInfo)
        } catch (e: Exception) {
            logger.e(e) { "An error occurred while fetching eur rate export info" }
            return Result.Error(DataError.Remote.UNKNOWN)
        }
    }
}

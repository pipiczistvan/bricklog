package hu.piware.bricklog.feature.set.data.firebase

import co.touchlab.kermit.Logger
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.set.domain.datasource.RemoteDataServiceDataSource
import hu.piware.bricklog.feature.set.domain.model.BatchExportInfo
import hu.piware.bricklog.feature.set.domain.model.Collectible
import hu.piware.bricklog.feature.set.domain.model.ExportInfo
import org.koin.core.annotation.Single

@Single
class FirestoreDataServiceDataSource : RemoteDataServiceDataSource {

    private val logger = Logger.withTag("FirestoreDataServiceDataSource")

    private val firestore = Firebase.firestore

    override suspend fun getExportInfo(): Result<ExportInfo, DataError> {
        try {
            val exportInfo = firestore
                .collection("bricklog-data-service")
                .document("export-info")
                .get()
                .data<ExportInfoDocument>()
                .toDomainModel()

            return Result.Success(exportInfo)
        } catch (e: Exception) {
            return Result.Error(DataError.Remote.UNKNOWN)
        }
    }

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
            val collectibleSnapshots = firestore
                .collection("collectibles")
                .get()
                .documents

            val collectibles = collectibleSnapshots.map { collectibleSnapshot ->
                val setNumber = collectibleSnapshot.id
                val collectibleName = collectibleSnapshot.get<String?>("name")

                val codeListSnapshots = firestore
                    .collection("collectibles/$setNumber/codes")
                    .get()
                    .documents

                Collectible(
                    setNumber = setNumber,
                    name = collectibleName ?: "",
                    codes = codeListSnapshots.associateBy(
                        keySelector = { it.id.toInt() },
                        valueTransform = { it.data<CodeListDocument>().toDomainModel() }
                    )
                )
            }

            return Result.Success(collectibles)
        } catch (e: Exception) {
            logger.e(e) { "An error occurred while fetching collectibles" }
            return Result.Error(DataError.Remote.UNKNOWN)
        }
    }
}

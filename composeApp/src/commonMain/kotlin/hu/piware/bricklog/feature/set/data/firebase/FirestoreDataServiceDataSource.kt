package hu.piware.bricklog.feature.set.data.firebase

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.set.domain.datasource.RemoteDataServiceDataSource
import hu.piware.bricklog.feature.set.domain.model.ExportInfo
import org.koin.core.annotation.Single

@Single
class FirestoreDataServiceDataSource : RemoteDataServiceDataSource {

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
}

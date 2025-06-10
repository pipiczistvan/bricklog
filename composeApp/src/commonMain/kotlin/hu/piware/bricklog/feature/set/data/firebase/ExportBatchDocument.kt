package hu.piware.bricklog.feature.set.data.firebase

import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.serialization.Serializable

@Serializable
data class ExportBatchDocument(
    val validFrom: Timestamp? = null,
    val validTo: Timestamp? = null,
    val rowCount: Int = 0,
    val fileUploads: List<FileUploadResultDocument> = emptyList(),
)

package hu.piware.bricklog.feature.set.data.firebase

import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.serialization.Serializable

@Serializable
data class ExportInfoDocument(
    val id: Int? = null,
    val fileUploads: List<FileUploadResultDocument> = emptyList(),
    val lastUpdated: Timestamp? = null,
)

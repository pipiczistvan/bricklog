package hu.piware.bricklog.feature.set.data.firebase

import kotlinx.serialization.Serializable

@Serializable
data class FileUploadResultDocument(
    val serviceId: String? = null,
    val url: String? = null,
    val fileId: String? = null,
    val priority: Int? = null,
)

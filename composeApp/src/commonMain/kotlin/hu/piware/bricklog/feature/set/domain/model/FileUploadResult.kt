package hu.piware.bricklog.feature.set.domain.model

data class FileUploadResult(
    val id: String,
    val url: String,
    val fileId: String,
    val priority: Int
)

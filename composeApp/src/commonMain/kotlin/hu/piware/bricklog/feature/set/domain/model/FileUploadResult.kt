package hu.piware.bricklog.feature.set.domain.model

data class FileUploadResult(
    val serviceId: String,
    val url: String,
    val fileId: String,
    val priority: Int,
)

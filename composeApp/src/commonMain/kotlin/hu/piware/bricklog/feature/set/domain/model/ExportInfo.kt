package hu.piware.bricklog.feature.set.domain.model

import kotlinx.datetime.Instant

data class ExportInfo(
    val id: Int,
    val fileUploads: List<FileUploadResult>,
    val lastUpdated: Instant,
)

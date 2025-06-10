package hu.piware.bricklog.feature.set.domain.model

import kotlinx.datetime.Instant

data class ExportBatch(
    val validFrom: Instant,
    val validTo: Instant,
    val rowCount: Int,
    val fileUploads: List<FileUploadResult>,
)

package hu.piware.bricklog.feature.set.domain.model

import kotlinx.datetime.Instant

data class ExportInfo(
    val id: Int,
    val fileUploads: List<FileUploadResult>,
    val lastUpdated: Instant,
)

fun ExportInfo.toExportBatch(): ExportBatch {
    return ExportBatch(
        validFrom = lastUpdated,
        validTo = lastUpdated,
        rowCount = 0,
        fileUploads = fileUploads,
    )
}

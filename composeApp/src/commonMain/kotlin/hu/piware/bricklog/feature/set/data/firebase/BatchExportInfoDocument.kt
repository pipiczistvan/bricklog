package hu.piware.bricklog.feature.set.data.firebase

import kotlinx.serialization.Serializable

@Serializable
data class BatchExportInfoDocument(
    val batches: List<ExportBatchDocument> = emptyList(),
)

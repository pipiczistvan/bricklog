package hu.piware.bricklog.feature.settings.domain.model

import hu.piware.bricklog.feature.core.presentation.util.formatDate
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

data class DataSyncInfo(
    val exportDate: Instant,
    val updateDate: Instant,
)

val DataSyncInfo.formattedExportDate: String
    get() = localExportDate?.let { formatDate(it) } ?: ""

val DataSyncInfo.formattedUpdateDate: String
    get() = localUpdateDate?.let { formatDate(it) } ?: ""

val DataSyncInfo.localExportDate: LocalDateTime?
    get() = exportDate.toLocalDateTime(TimeZone.currentSystemDefault())

val DataSyncInfo.localUpdateDate: LocalDateTime?
    get() = updateDate.toLocalDateTime(TimeZone.currentSystemDefault())

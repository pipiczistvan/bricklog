package hu.piware.bricklog.feature.core.presentation.util

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

actual fun formatDateTime(localDateTime: LocalDateTime): String {
    return DateTimeFormatter
        .ofPattern("yyyy/MM/dd - HH:mm", Locale.getDefault())
        .format(localDateTime.toJavaLocalDateTime())
}

actual fun formatDate(localDateTime: LocalDateTime): String {
    return DateTimeFormatter
        .ofPattern("yyyy/MM/dd", Locale.getDefault())
        .format(localDateTime.toJavaLocalDateTime())
}

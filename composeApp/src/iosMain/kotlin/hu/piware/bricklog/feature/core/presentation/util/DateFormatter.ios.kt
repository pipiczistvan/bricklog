package hu.piware.bricklog.feature.core.presentation.util

import kotlinx.datetime.LocalDateTime
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSLocale
import platform.Foundation.currentLocale

actual fun formatDateTime(localDateTime: LocalDateTime): String {
    val date = localDateTime.toNsDate()
        ?: throw IllegalStateException("Failed to convert LocalDateTime $LocalDateTime to NSDate")
    val formatter = NSDateFormatter().apply {
        dateFormat = "yyyy/MM/dd - HH:mm"
        locale = NSLocale.currentLocale
    }
    return formatter.stringFromDate(date)
}

actual fun formatDate(localDateTime: LocalDateTime): String {
    val date = localDateTime.toNsDate()
        ?: throw IllegalStateException("Failed to convert LocalDateTime $LocalDateTime to NSDate")
    val formatter = NSDateFormatter().apply {
        dateFormat = "yyyy/MM/dd"
        locale = NSLocale.currentLocale
    }
    return formatter.stringFromDate(date)
}

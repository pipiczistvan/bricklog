package hu.piware.bricklog.feature.core.presentation.util

import kotlinx.datetime.LocalDateTime

expect fun formatDateTime(localDateTime: LocalDateTime): String
expect fun formatDate(localDateTime: LocalDateTime): String

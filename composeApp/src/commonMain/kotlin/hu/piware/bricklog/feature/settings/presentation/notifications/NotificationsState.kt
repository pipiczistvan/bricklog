package hu.piware.bricklog.feature.settings.presentation.notifications

import hu.piware.bricklog.feature.settings.domain.model.NotificationPreferences

data class NotificationsState(
    val notificationPermissionGranted: Boolean = false,
    val notificationPreferences: NotificationPreferences = NotificationPreferences(),
)

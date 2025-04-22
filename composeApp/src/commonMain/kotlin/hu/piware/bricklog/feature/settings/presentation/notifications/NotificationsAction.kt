package hu.piware.bricklog.feature.settings.presentation.notifications

import hu.piware.bricklog.feature.settings.domain.model.NotificationPreferences

sealed interface NotificationsAction {
    data object OnBackClick : NotificationsAction
    data object OpenSettings : NotificationsAction
    data class OnNotificationPreferenceChange(val preferences: NotificationPreferences) :
        NotificationsAction
}

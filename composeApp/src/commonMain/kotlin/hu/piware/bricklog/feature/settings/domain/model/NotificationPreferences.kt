package hu.piware.bricklog.feature.settings.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class NotificationPreferences(
    val newSets: Boolean = true,
) {
    companion object {
        fun allEnabled() = NotificationPreferences(newSets = true)
        fun allDisabled() = NotificationPreferences(newSets = false)
    }
}

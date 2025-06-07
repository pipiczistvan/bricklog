package hu.piware.bricklog.feature.settings.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class NotificationPreferences(
    val general: Boolean = true,
    val newSets: Boolean = true,
)

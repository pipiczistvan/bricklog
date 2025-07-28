package hu.piware.bricklog.feature.settings.data.firebase

import kotlinx.serialization.Serializable

@Serializable
data class UserPreferencesDocument(
    val hideGreetings: Boolean = false,
    val displayName: String? = null,
    val collectionOrder: List<String> = emptyList(),
)

package hu.piware.bricklog.feature.settings.data.firebase

import kotlinx.serialization.Serializable

@Serializable
data class UserPreferencesDocument(
    val showGreetings: Boolean = true,
    val displayName: String? = null,
    val collectionOrder: List<String> = emptyList(),
)

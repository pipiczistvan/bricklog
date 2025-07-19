package hu.piware.bricklog.feature.settings.data.firebase

import kotlinx.serialization.Serializable

@Serializable
data class UserPreferencesDocument(
    val showGreetings: Boolean? = null,
    val displayName: String? = null,
)

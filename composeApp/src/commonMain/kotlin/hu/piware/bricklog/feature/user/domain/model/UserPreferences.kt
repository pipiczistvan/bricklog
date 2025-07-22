package hu.piware.bricklog.feature.user.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class UserPreferences(
    val showGreetings: Boolean = false,
    val displayName: String? = null,
)
package hu.piware.bricklog.feature.user.domain.model

import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import kotlinx.serialization.Serializable

@Serializable
data class UserPreferences(
    val showGreetings: Boolean = false,
    val displayName: String? = null,
    val collectionOrder: List<CollectionId> = emptyList(),
)

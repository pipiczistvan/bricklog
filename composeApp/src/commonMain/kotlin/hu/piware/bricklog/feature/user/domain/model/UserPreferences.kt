package hu.piware.bricklog.feature.user.domain.model

import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.set.presentation.dashboard.utils.FeaturedSetType
import kotlinx.serialization.Serializable

@Serializable
data class UserPreferences(
    val hideGreetings: Boolean = false,
    val displayName: String? = null,
    val collectionOrder: List<CollectionId> = emptyList(),
    val hiddenFeaturedSets: List<FeaturedSetType> = emptyList(),
)

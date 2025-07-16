package hu.piware.bricklog.feature.collection.data.firebase

import hu.piware.bricklog.feature.collection.domain.model.CollectionIcon
import kotlinx.serialization.Serializable

@Serializable
data class CollectionDocument(
    val name: String,
    val icon: CollectionIcon,
)

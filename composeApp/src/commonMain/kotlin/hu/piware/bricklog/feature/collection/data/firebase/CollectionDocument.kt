package hu.piware.bricklog.feature.collection.data.firebase

import hu.piware.bricklog.feature.collection.domain.model.CollectionIcon
import hu.piware.bricklog.feature.collection.domain.model.CollectionType
import hu.piware.bricklog.feature.user.domain.model.UserId
import kotlinx.serialization.Serializable

@Serializable
data class CollectionDocument(
    val owner: UserId,
    val name: String,
    val icon: CollectionIcon,
    val type: CollectionType,
    val shares: Map<UserId, CollectionShareDocument>,
    val sharedWith: List<UserId>,
)

@Serializable
data class CollectionShareDocument(
    val canWrite: Boolean,
)

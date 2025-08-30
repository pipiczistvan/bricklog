package hu.piware.bricklog.feature.collection.presentation.collection_share_edit

import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.user.domain.model.UserId
import kotlinx.serialization.Serializable

@Serializable
data class CollectionShareEditArguments(
    val collectionId: CollectionId,
    val userId: UserId?,
)

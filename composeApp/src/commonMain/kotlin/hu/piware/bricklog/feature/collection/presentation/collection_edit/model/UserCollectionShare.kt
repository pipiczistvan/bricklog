package hu.piware.bricklog.feature.collection.presentation.collection_edit.model

import hu.piware.bricklog.feature.collection.domain.model.CollectionShare
import hu.piware.bricklog.feature.user.domain.model.UserId

data class UserCollectionShare(
    val userId: UserId,
    val share: CollectionShare,
)

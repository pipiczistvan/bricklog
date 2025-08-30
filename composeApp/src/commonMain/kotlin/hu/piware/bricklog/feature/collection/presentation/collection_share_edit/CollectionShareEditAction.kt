package hu.piware.bricklog.feature.collection.presentation.collection_share_edit

import hu.piware.bricklog.feature.collection.domain.model.UserCollectionShare
import hu.piware.bricklog.feature.user.domain.model.UserId

sealed interface CollectionShareEditAction {
    data object OnBackClick : CollectionShareEditAction
    data class OnFriendEditClick(val friendId: UserId) : CollectionShareEditAction
    data class OnShareChange(val share: UserCollectionShare) : CollectionShareEditAction
    data class OnShareDelete(val share: UserCollectionShare) : CollectionShareEditAction
}

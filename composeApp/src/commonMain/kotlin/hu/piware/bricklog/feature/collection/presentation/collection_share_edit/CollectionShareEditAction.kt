package hu.piware.bricklog.feature.collection.presentation.collection_share_edit

import hu.piware.bricklog.feature.collection.domain.model.UserCollectionShare

sealed interface CollectionShareEditAction {
    data object OnBackClick : CollectionShareEditAction
    data class OnShareChange(val share: UserCollectionShare) : CollectionShareEditAction
    data class OnShareDelete(val share: UserCollectionShare) : CollectionShareEditAction
}

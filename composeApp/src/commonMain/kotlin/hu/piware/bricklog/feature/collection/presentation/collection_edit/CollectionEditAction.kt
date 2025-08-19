package hu.piware.bricklog.feature.collection.presentation.collection_edit

import hu.piware.bricklog.feature.collection.domain.model.CollectionIcon
import hu.piware.bricklog.feature.collection.presentation.collection_edit.model.UserCollectionShare

sealed interface CollectionEditAction {
    data object OnBackClick : CollectionEditAction
    data class OnNameChanged(val name: String) : CollectionEditAction
    data class OnIconChanged(val icon: CollectionIcon) : CollectionEditAction
    data object OnDeleteClick : CollectionEditAction
    data object OnSubmit : CollectionEditAction
    data class OnShareChanged(val share: UserCollectionShare) : CollectionEditAction
    data class OnShareDeleted(val share: UserCollectionShare) : CollectionEditAction
}

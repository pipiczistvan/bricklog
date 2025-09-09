package hu.piware.bricklog.feature.collection.presentation.collection_edit

import hu.piware.bricklog.feature.collection.domain.model.Collection
import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.user.domain.model.UserId

sealed interface CollectionEditAction {
    data object OnBackClick : CollectionEditAction

    data class OnCollectionChange(val collection: Collection) :
        CollectionEditAction

    data class OnCollectionDelete(val collection: Collection) : CollectionEditAction

    data class OnShareClick(
        val collectionId: CollectionId,
        val userId: UserId?,
    ) : CollectionEditAction
}

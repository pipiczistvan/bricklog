package hu.piware.bricklog.feature.set.presentation.set_detail

import hu.piware.bricklog.feature.collection.domain.model.CollectionDetails
import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.set.domain.model.SetId

sealed interface SetDetailAction {
    data object OnBackClick : SetDetailAction
    data class OnCollectionToggle(val setId: SetId, val collectionId: CollectionId) :
        SetDetailAction

    data class OnImageClick(val setId: SetId) : SetDetailAction

    data class OnPreferredCollectionChange(val collection: CollectionDetails) : SetDetailAction
}

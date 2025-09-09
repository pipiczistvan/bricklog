package hu.piware.bricklog.feature.collection.presentation.collection_list

import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.collection.domain.model.CollectionSetDetails

sealed interface CollectionListAction {
    data object OnBackClick : CollectionListAction
    data class OnCollectionClick(val id: CollectionId) : CollectionListAction
    data class OnCollectionEditClick(val id: CollectionId?) : CollectionListAction
    data class OnCollectionOrderChange(val collections: List<CollectionSetDetails>) :
        CollectionListAction
}

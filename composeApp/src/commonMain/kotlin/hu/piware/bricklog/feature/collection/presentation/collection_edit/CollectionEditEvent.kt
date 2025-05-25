package hu.piware.bricklog.feature.collection.presentation.collection_edit

sealed interface CollectionEditEvent {
    data object Back : CollectionEditEvent
}

package hu.piware.bricklog.feature.collection.presentation.collection_edit

import hu.piware.bricklog.feature.collection.domain.model.Collection
import hu.piware.bricklog.feature.collection.domain.model.CollectionIcon
import hu.piware.bricklog.feature.core.presentation.UiText

data class CollectionEditState(
    val collection: Collection? = null,
    val name: String = "",
    val nameError: UiText? = null,
    val icon: CollectionIcon = CollectionIcon.STAR,
)

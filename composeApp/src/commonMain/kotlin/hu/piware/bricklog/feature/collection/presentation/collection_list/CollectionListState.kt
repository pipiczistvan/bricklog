package hu.piware.bricklog.feature.collection.presentation.collection_list

import hu.piware.bricklog.feature.collection.domain.model.CollectionSetDetails
import hu.piware.bricklog.feature.user.domain.manager.SessionManager.Companion.GUEST_USER
import hu.piware.bricklog.feature.user.domain.model.User

data class CollectionListState(
    val collections: List<CollectionSetDetails> = emptyList(),
    val currentUser: User = GUEST_USER,
)

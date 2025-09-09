package hu.piware.bricklog.feature.collection.presentation.collection_edit

import hu.piware.bricklog.feature.collection.domain.model.Collection
import hu.piware.bricklog.feature.user.domain.manager.SessionManager.Companion.GUEST_USER
import hu.piware.bricklog.feature.user.domain.model.User

data class CollectionEditState(
    val isLoading: Boolean = false,
    val currentUser: User = GUEST_USER,
    val collection: Collection? = null,
)

val CollectionEditState.isNew: Boolean
    get() = collection == null

val CollectionEditState.isOwner: Boolean
    get() = collection != null && collection.owner == currentUser.uid

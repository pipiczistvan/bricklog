package hu.piware.bricklog.feature.collection.presentation.collection_edit

import hu.piware.bricklog.feature.collection.domain.model.Collection
import hu.piware.bricklog.feature.collection.domain.model.CollectionIcon
import hu.piware.bricklog.feature.collection.domain.model.CollectionShare
import hu.piware.bricklog.feature.core.presentation.UiText
import hu.piware.bricklog.feature.user.domain.manager.SessionManager.Companion.GUEST_USER
import hu.piware.bricklog.feature.user.domain.model.User
import hu.piware.bricklog.feature.user.domain.model.UserId

data class CollectionEditState(
    val currentUser: User = GUEST_USER,
    val collection: Collection? = null,
    val name: String = "",
    val nameError: UiText? = null,
    val icon: CollectionIcon = CollectionIcon.STAR,
    val shares: Map<UserId, CollectionShare> = emptyMap(),
)

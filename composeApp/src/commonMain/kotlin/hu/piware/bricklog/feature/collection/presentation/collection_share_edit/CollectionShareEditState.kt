package hu.piware.bricklog.feature.collection.presentation.collection_share_edit

import hu.piware.bricklog.feature.collection.domain.model.Collection
import hu.piware.bricklog.feature.collection.domain.model.UserCollectionShare
import hu.piware.bricklog.feature.user.domain.model.Friend
import hu.piware.bricklog.feature.user.domain.model.UserId

data class CollectionShareEditState(
    val isLoading: Boolean = false,
    val collection: Collection? = null,
    val share: UserCollectionShare? = null,
    val friends: List<Friend> = emptyList(),
    val shareUserIdArgument: UserId = "",
)

val CollectionShareEditState.isNew: Boolean
    get() = share == null

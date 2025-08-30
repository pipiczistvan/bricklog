package hu.piware.bricklog.mock

import hu.piware.bricklog.feature.collection.domain.model.Collection
import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.set.domain.model.SetId
import hu.piware.bricklog.feature.user.domain.model.Friend
import hu.piware.bricklog.feature.user.domain.model.UserId
import hu.piware.bricklog.feature.user.domain.model.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

object MockFirestore {
    val userPreferences = MutableStateFlow(mapOf<UserId, UserPreferences>())
    val collections = MutableStateFlow(listOf<Collection>())
    val userSetCollections = MutableStateFlow(mapOf<UserId, Map<SetId, List<CollectionId>>>())
    val userFriends = MutableStateFlow(mapOf<UserId, List<Friend>>())

    fun clear() {
        userPreferences.update { emptyMap() }
        collections.update { listOf() }
        userSetCollections.update { emptyMap() }
        userFriends.update { emptyMap() }
    }
}

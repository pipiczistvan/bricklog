package hu.piware.bricklog.mock

import hu.piware.bricklog.feature.collection.domain.model.Collection
import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.set.domain.model.SetId
import hu.piware.bricklog.feature.user.domain.model.UserId
import hu.piware.bricklog.feature.user.domain.model.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

object MockFirestore {
    val userPreferences = MutableStateFlow<UserPreferences?>(null)
    val userCollections = MutableStateFlow(mapOf<UserId, List<Collection>>())
    val setCollections = MutableStateFlow(mapOf<UserId, Map<SetId, List<CollectionId>>>())

    fun clear() {
        userPreferences.update { null }
        userCollections.update { emptyMap() }
        setCollections.update { emptyMap() }
    }
}

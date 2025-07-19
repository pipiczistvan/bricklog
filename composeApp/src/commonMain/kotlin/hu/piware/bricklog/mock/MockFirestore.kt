package hu.piware.bricklog.mock

import hu.piware.bricklog.feature.collection.domain.model.Collection
import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.set.domain.model.SetId
import hu.piware.bricklog.feature.settings.domain.model.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

object MockFirestore {
    var userPreferences = MutableStateFlow<UserPreferences?>(null)
    var collections = MutableStateFlow(listOf<Collection>())
    var setCollections = MutableStateFlow(mapOf<SetId, List<CollectionId>>())

    fun clear() {
        userPreferences.update { null }
        collections.update { emptyList() }
        setCollections.update { emptyMap() }
    }
}

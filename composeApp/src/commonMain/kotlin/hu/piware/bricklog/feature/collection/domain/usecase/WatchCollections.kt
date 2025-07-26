@file:OptIn(ExperimentalCoroutinesApi::class)

package hu.piware.bricklog.feature.collection.domain.usecase

import hu.piware.bricklog.feature.collection.domain.model.Collection
import hu.piware.bricklog.feature.collection.domain.repository.CollectionRepository
import hu.piware.bricklog.feature.user.domain.model.UserPreferences
import hu.piware.bricklog.feature.user.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single
class WatchCollections(
    private val collectionRepository: CollectionRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
) {
    operator fun invoke(): Flow<List<Collection>> {
        return userPreferencesRepository.watchUserPreferences()
            .flatMapLatest { userPreferences ->
                collectionRepository.watchCollections()
                    .sorted(userPreferences)
            }
    }

    private fun Flow<List<Collection>>.sorted(userPreferences: UserPreferences) =
        map { collections ->
            collections.sortedWith { collection1, collection2 ->
                val index1 = userPreferences.collectionOrder.indexOf(collection1.id)
                    .let { if (it == -1) Int.MAX_VALUE else it }
                val index2 = userPreferences.collectionOrder.indexOf(collection2.id)
                    .let { if (it == -1) Int.MAX_VALUE else it }
                index1.compareTo(index2)
            }
        }
}

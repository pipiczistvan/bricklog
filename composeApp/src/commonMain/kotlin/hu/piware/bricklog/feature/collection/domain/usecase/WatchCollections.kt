@file:OptIn(ExperimentalCoroutinesApi::class)

package hu.piware.bricklog.feature.collection.domain.usecase

import hu.piware.bricklog.feature.collection.domain.model.Collection
import hu.piware.bricklog.feature.collection.domain.model.CollectionType
import hu.piware.bricklog.feature.collection.domain.repository.CollectionRepository
import hu.piware.bricklog.feature.set.domain.model.SetId
import hu.piware.bricklog.feature.user.domain.manager.SessionManager
import hu.piware.bricklog.feature.user.domain.manager.userBoundFlow
import hu.piware.bricklog.feature.user.domain.model.UserId
import hu.piware.bricklog.feature.user.domain.model.UserPreferences
import hu.piware.bricklog.feature.user.domain.usecase.WatchUserPreferences
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single
class WatchCollections(
    private val collectionRepository: CollectionRepository,
    private val watchUserPreferences: WatchUserPreferences,
    private val sessionManager: SessionManager,
) {
    operator fun invoke(
        userId: UserId? = null,
        type: CollectionType? = null,
        setId: SetId? = null,
    ): Flow<List<Collection>> {
        return sessionManager.userBoundFlow(userId) { userId ->
            watchUserPreferences(userId)
                .flatMapLatest { userPreferences ->
                    collectionRepository.watchCollections(userId, type, setId)
                        .sorted(userPreferences)
                }
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

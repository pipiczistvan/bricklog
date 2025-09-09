@file:OptIn(ExperimentalCoroutinesApi::class)

package hu.piware.bricklog.feature.collection.domain.usecase

import hu.piware.bricklog.feature.collection.domain.model.CollectionSetDetails
import hu.piware.bricklog.feature.set.domain.usecase.WatchSetDetailsByPreferences
import hu.piware.bricklog.feature.settings.domain.model.DEFAULT_SET_FILTER_PREFERENCES
import hu.piware.bricklog.feature.settings.domain.model.toSetFilter
import hu.piware.bricklog.feature.user.domain.manager.SessionManager
import hu.piware.bricklog.feature.user.domain.manager.userBoundFlow
import hu.piware.bricklog.feature.user.domain.model.UserId
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

private val collectionSetDetailsFilter = DEFAULT_SET_FILTER_PREFERENCES.copy().toSetFilter()

@Single
class WatchCollectionSetDetails(
    private val watchCollectionDetails: WatchCollectionDetails,
    private val watchSetDetailsByPreferences: WatchSetDetailsByPreferences,
    private val sessionManager: SessionManager,
) {
    operator fun invoke(
        collectionLimit: Int = Int.MAX_VALUE,
        setLimit: Int,
        userId: UserId? = null,
    ): Flow<List<CollectionSetDetails>> {
        return sessionManager.userBoundFlow(userId) { userId ->
            watchCollectionDetails(userId).flatMapLatest { collectionDetailsList ->
                val flows = collectionDetailsList
                    .take(collectionLimit) // TODO: Limit in data layer
                    .map { collectionDetails ->
                        watchSetDetailsByPreferences(
                            filterOverrides = collectionSetDetailsFilter.copy(
                                collectionIds = listOf(collectionDetails.collection.id),
                                limit = setLimit,
                            ),
                        ).map { sets ->
                            CollectionSetDetails(
                                collection = collectionDetails,
                                sets = sets,
                            )
                        }
                    }

                combine(flows) { it.toList() }
            }
        }
    }
}

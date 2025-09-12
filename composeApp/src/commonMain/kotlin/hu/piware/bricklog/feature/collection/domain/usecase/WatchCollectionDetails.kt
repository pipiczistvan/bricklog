@file:OptIn(ExperimentalCoroutinesApi::class)

package hu.piware.bricklog.feature.collection.domain.usecase

import hu.piware.bricklog.feature.collection.domain.model.Collection
import hu.piware.bricklog.feature.collection.domain.model.CollectionDetails
import hu.piware.bricklog.feature.collection.domain.model.CollectionType
import hu.piware.bricklog.feature.collection.domain.model.toCollectionDetails
import hu.piware.bricklog.feature.set.domain.model.SetId
import hu.piware.bricklog.feature.user.domain.manager.SessionManager
import hu.piware.bricklog.feature.user.domain.manager.userBoundFlow
import hu.piware.bricklog.feature.user.domain.model.Friend
import hu.piware.bricklog.feature.user.domain.model.UserId
import hu.piware.bricklog.feature.user.domain.usecase.WatchFriends
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single
class WatchCollectionDetails(
    private val watchCollections: WatchCollections,
    private val watchFriends: WatchFriends,
    private val sessionManager: SessionManager,
) {
    operator fun invoke(
        userId: UserId? = null,
        type: CollectionType? = null,
        setId: SetId? = null,
    ): Flow<List<CollectionDetails>> {
        return sessionManager.userBoundFlow { userId ->
            watchFriends().flatMapLatest { friends ->
                watchCollections(
                    userId = userId,
                    type = type,
                    setId = setId,
                ).map { it.toCollectionDetails(userId, friends) }
            }
        }
    }
}

private fun List<Collection>.toCollectionDetails(userId: UserId, friends: List<Friend>) =
    map { it.toCollectionDetails(userId, friends) }

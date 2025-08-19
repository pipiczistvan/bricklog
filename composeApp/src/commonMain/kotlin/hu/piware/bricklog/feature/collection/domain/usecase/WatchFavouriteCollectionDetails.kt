package hu.piware.bricklog.feature.collection.domain.usecase

import hu.piware.bricklog.feature.collection.domain.model.CollectionDetails
import hu.piware.bricklog.feature.collection.domain.model.CollectionType
import hu.piware.bricklog.feature.collection.domain.model.toCollectionDetails
import hu.piware.bricklog.feature.user.domain.manager.SessionManager
import hu.piware.bricklog.feature.user.domain.manager.userBoundFlow
import hu.piware.bricklog.feature.user.domain.model.UserId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single
class WatchFavouriteCollectionDetails(
    private val watchCollections: WatchCollections,
    private val sessionManager: SessionManager,
) {
    operator fun invoke(userId: UserId? = null): Flow<CollectionDetails?> {
        return sessionManager.userBoundFlow(userId) { userId ->
            watchCollections(
                userId = userId,
                type = CollectionType.FAVOURITE,
            )
                .map { collections -> collections.firstOrNull { it.owner == userId } }
                .map { it?.toCollectionDetails(userId) }
        }
    }
}

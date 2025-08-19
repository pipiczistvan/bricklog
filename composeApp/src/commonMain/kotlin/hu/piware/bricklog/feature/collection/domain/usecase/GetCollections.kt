package hu.piware.bricklog.feature.collection.domain.usecase

import hu.piware.bricklog.feature.collection.domain.model.Collection
import hu.piware.bricklog.feature.collection.domain.model.CollectionType
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.set.domain.model.SetId
import hu.piware.bricklog.feature.user.domain.manager.SessionManager
import hu.piware.bricklog.feature.user.domain.model.UserId
import hu.piware.bricklog.util.asResultOrDefault
import org.koin.core.annotation.Single

@Single
class GetCollections(
    private val watchCollections: WatchCollections,
    private val sessionManager: SessionManager,
) {
    suspend operator fun invoke(
        userId: UserId = sessionManager.currentUserId,
        type: CollectionType? = null,
        setId: SetId? = null,
    ): Result<List<Collection>, DataError> {
        return watchCollections(
            userId = userId,
            type = type,
            setId = setId,
        ).asResultOrDefault { emptyList() }
    }
}

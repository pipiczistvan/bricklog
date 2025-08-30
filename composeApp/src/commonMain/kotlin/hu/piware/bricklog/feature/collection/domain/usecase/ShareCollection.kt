package hu.piware.bricklog.feature.collection.domain.usecase

import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.collection.domain.model.UserCollectionShare
import hu.piware.bricklog.feature.collection.domain.repository.CollectionRepository
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.user.domain.manager.SessionManager
import hu.piware.bricklog.feature.user.domain.model.UserId
import org.koin.core.annotation.Single

@Single
class ShareCollection(
    private val collectionRepository: CollectionRepository,
    private val sessionManager: SessionManager,
) {
    suspend operator fun invoke(
        collectionId: CollectionId,
        share: UserCollectionShare,
        userId: UserId = sessionManager.currentUserId,
    ): EmptyResult<DataError> {
        return collectionRepository.saveCollectionShare(
            userId = userId,
            collectionId = collectionId,
            share = share,
        )
    }
}

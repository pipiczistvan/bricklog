package hu.piware.bricklog.feature.collection.domain.usecase

import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.collection.domain.repository.CollectionRepository
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.set.domain.model.SetId
import hu.piware.bricklog.feature.user.domain.manager.SessionManager
import hu.piware.bricklog.feature.user.domain.model.UserId
import kotlinx.coroutines.flow.firstOrNull
import org.koin.core.annotation.Single

@Single
class ToggleCollectionSet(
    private val watchCollections: WatchCollections,
    private val collectionRepository: CollectionRepository,
    private val sessionManager: SessionManager,
) {
    suspend operator fun invoke(
        setId: SetId,
        collectionID: CollectionId,
        userId: UserId = sessionManager.currentUserId,
    ): EmptyResult<DataError> {
        val setCollections = watchCollections(setId = setId)
            .firstOrNull()

        val setIsInCollection = setCollections?.any { it.id == collectionID } ?: false

        return if (setIsInCollection) {
            collectionRepository.removeSetFromCollections(userId, setId, listOf(collectionID))
        } else {
            collectionRepository.addSetToCollections(userId, setId, listOf(collectionID))
        }
    }
}

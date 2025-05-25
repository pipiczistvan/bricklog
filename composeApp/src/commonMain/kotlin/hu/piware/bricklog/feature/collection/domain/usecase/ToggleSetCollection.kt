package hu.piware.bricklog.feature.collection.domain.usecase

import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.collection.domain.repository.CollectionRepository
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.set.domain.model.SetId
import kotlinx.coroutines.flow.firstOrNull
import org.koin.core.annotation.Single

@Single
class ToggleSetCollection(
    private val collectionRepository: CollectionRepository,
) {
    suspend operator fun invoke(setID: SetId, collectionID: CollectionId): EmptyResult<DataError> {
        val setCollections = collectionRepository.watchCollectionsBySet(setID)
            .firstOrNull()

        val setIsInCollection = setCollections?.any { it.id == collectionID } ?: false

        return if (setIsInCollection) {
            collectionRepository.removeSetFromCollection(setID, collectionID)
        } else {
            collectionRepository.addSetToCollection(setID, collectionID)
        }
    }
}

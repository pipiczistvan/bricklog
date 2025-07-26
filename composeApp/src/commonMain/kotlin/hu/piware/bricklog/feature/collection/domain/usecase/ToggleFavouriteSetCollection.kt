package hu.piware.bricklog.feature.collection.domain.usecase

import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.collection.domain.model.CollectionType
import hu.piware.bricklog.feature.collection.domain.repository.CollectionRepository
import hu.piware.bricklog.feature.collection.domain.util.defaultCollections
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.core.domain.data
import hu.piware.bricklog.feature.core.domain.first
import hu.piware.bricklog.feature.core.domain.onError
import hu.piware.bricklog.feature.set.domain.model.SetId
import hu.piware.bricklog.util.asResultOrDefault
import kotlinx.coroutines.flow.firstOrNull
import org.koin.core.annotation.Single

@Single
class ToggleFavouriteSetCollection(
    private val collectionRepository: CollectionRepository,
) {
    suspend operator fun invoke(setId: SetId): EmptyResult<DataError> {
        val favouriteSetCollection =
            collectionRepository.watchCollections(CollectionType.FAVOURITE)
                .asResultOrDefault { emptyList() }
                .onError { return it }
                .data()
                .firstOrNull()?.id ?: createFavouriteCollection()
                .onError { return it }
                .data()

        val setCollections = collectionRepository.watchCollections(setId = setId)
            .firstOrNull()

        val setIsInCollection = setCollections?.any { it.id == favouriteSetCollection } ?: false

        return if (setIsInCollection) {
            collectionRepository.removeSetFromCollections(setId, listOf(favouriteSetCollection))
        } else {
            collectionRepository.addSetToCollections(setId, listOf(favouriteSetCollection))
        }
    }

    private suspend fun createFavouriteCollection(): Result<CollectionId, DataError> {
        val defaultCollections = defaultCollections.filter { it.type == CollectionType.FAVOURITE }

        return collectionRepository.saveCollections(defaultCollections).first()
    }
}

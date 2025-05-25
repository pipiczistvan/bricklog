package hu.piware.bricklog.feature.collection.domain.usecase

import hu.piware.bricklog.feature.collection.domain.model.Collection
import hu.piware.bricklog.feature.collection.domain.repository.CollectionRepository
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import org.koin.core.annotation.Single

@Single
class SaveCollection(
    private val collectionRepository: CollectionRepository,
) {
    suspend operator fun invoke(collection: Collection): EmptyResult<DataError> {
        return collectionRepository.saveCollection(collection)
    }
}

package hu.piware.bricklog.feature.collection.domain.usecase

import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.collection.domain.repository.CollectionRepository
import hu.piware.bricklog.feature.collection.domain.util.defaultCollections
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import org.koin.core.annotation.Single

@Single
class DeleteCollection(
    private val collectionRepository: CollectionRepository,
) {
    suspend operator fun invoke(id: CollectionId): EmptyResult<DataError> {
        if (id == "" || defaultCollections.any { it.id == id }) {
            return Result.Error(DataError.Local.UNKNOWN)
        }

        return collectionRepository.deleteCollections(listOf(id))
    }
}

package hu.piware.bricklog.feature.collection.domain.usecase

import hu.piware.bricklog.feature.collection.domain.model.Collection
import hu.piware.bricklog.feature.collection.domain.repository.CollectionRepository
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.core.domain.first
import org.koin.core.annotation.Single

@Single
class SaveCollection(
    private val collectionRepository: CollectionRepository,
) {
    suspend operator fun invoke(collection: Collection): Result<Collection, DataError> {
        return collectionRepository.saveCollections(listOf(collection)).first()
    }
}

package hu.piware.bricklog.feature.collection.domain.usecase

import hu.piware.bricklog.feature.collection.domain.model.Collection
import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.collection.domain.repository.CollectionRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class WatchCollection(
    private val collectionRepository: CollectionRepository,
) {
    operator fun invoke(id: CollectionId): Flow<Collection> {
        return collectionRepository.watchCollection(id)
    }
}

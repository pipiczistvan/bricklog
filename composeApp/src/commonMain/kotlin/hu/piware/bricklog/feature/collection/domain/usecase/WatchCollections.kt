package hu.piware.bricklog.feature.collection.domain.usecase

import hu.piware.bricklog.feature.collection.domain.model.Collection
import hu.piware.bricklog.feature.collection.domain.repository.CollectionRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class WatchCollections(
    private val collectionRepository: CollectionRepository,
) {
    operator fun invoke(): Flow<List<Collection>> {
        return collectionRepository.watchCollections()
    }
}

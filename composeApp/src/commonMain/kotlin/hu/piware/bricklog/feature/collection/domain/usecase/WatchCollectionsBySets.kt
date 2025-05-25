package hu.piware.bricklog.feature.collection.domain.usecase

import hu.piware.bricklog.feature.collection.domain.model.Collection
import hu.piware.bricklog.feature.collection.domain.repository.CollectionRepository
import hu.piware.bricklog.feature.set.domain.model.SetId
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class WatchCollectionsBySets(
    private val collectionRepository: CollectionRepository,
) {
    operator fun invoke(): Flow<Map<SetId, List<Collection>>> {
        return collectionRepository.watchCollectionsBySets()
    }
}

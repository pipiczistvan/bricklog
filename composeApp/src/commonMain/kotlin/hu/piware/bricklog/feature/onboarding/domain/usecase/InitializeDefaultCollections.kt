package hu.piware.bricklog.feature.onboarding.domain.usecase

import hu.piware.bricklog.feature.collection.domain.repository.CollectionRepository
import hu.piware.bricklog.feature.collection.domain.util.defaultCollections
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.core.domain.onError
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Single

@Single
class InitializeDefaultCollections(
    private val collectionRepository: CollectionRepository,
) {
    suspend operator fun invoke(): EmptyResult<DataError> {
        val collections = collectionRepository.watchCollections().first()

        defaultCollections.forEach { defaultCollection ->
            if (collections.none { it.id == defaultCollection.id }) {
                collectionRepository.saveCollection(defaultCollection)
                    .onError { return it }
            }
        }

        return Result.Success(Unit)
    }
}

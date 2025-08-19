package hu.piware.bricklog.feature.collection.domain.usecase

import hu.piware.bricklog.feature.collection.domain.model.Collection
import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.util.asResult
import org.koin.core.annotation.Single

@Single
class GetCollection(
    private val watchCollection: WatchCollection,
) {
    suspend operator fun invoke(id: CollectionId): Result<Collection, DataError> {
        return watchCollection(id).asResult()
    }
}

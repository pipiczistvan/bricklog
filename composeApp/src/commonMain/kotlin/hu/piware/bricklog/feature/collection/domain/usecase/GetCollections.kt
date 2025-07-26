package hu.piware.bricklog.feature.collection.domain.usecase

import hu.piware.bricklog.feature.collection.domain.model.Collection
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.util.asResultOrDefault
import org.koin.core.annotation.Single

@Single
class GetCollections(
    private val watchCollections: WatchCollections,
) {
    suspend operator fun invoke(): Result<List<Collection>, DataError> {
        return watchCollections()
            .asResultOrDefault { emptyList() }
    }
}

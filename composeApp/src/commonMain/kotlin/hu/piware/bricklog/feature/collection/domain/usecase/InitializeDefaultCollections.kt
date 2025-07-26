package hu.piware.bricklog.feature.collection.domain.usecase

import hu.piware.bricklog.feature.collection.domain.util.defaultCollections
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.core.domain.asEmptyDataResult
import hu.piware.bricklog.feature.core.domain.data
import hu.piware.bricklog.feature.core.domain.onError
import org.koin.core.annotation.Single

@Single
class InitializeDefaultCollections(
    private val getCollections: GetCollections,
    private val saveCollections: SaveCollections,
) {
    suspend operator fun invoke(): EmptyResult<DataError> {
        val collections = getCollections()
            .onError { return it }
            .data()

        val missingCollections = defaultCollections
            .filter { defaultCollection -> collections.none { it.type == defaultCollection.type } }

        if (missingCollections.isEmpty()) {
            return Result.Success(Unit)
        } else {
            return saveCollections(*missingCollections.toTypedArray())
                .asEmptyDataResult()
        }
    }
}

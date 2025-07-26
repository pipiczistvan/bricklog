package hu.piware.bricklog.feature.collection.domain.usecase

import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.collection.domain.repository.CollectionRepository
import hu.piware.bricklog.feature.collection.domain.util.defaultCollections
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.core.domain.data
import hu.piware.bricklog.feature.core.domain.onError
import hu.piware.bricklog.feature.user.domain.usecase.GetUserPreferences
import hu.piware.bricklog.feature.user.domain.usecase.SaveUserPreferences
import org.koin.core.annotation.Single

@Single
class DeleteCollections(
    private val collectionRepository: CollectionRepository,
    private val getUserPreferences: GetUserPreferences,
    private val saveUserPreferences: SaveUserPreferences,
) {
    suspend operator fun invoke(vararg collectionIds: CollectionId): EmptyResult<DataError> {
        if (collectionIds.any { collectionId -> collectionId == "" || defaultCollections.any { it.id == collectionId } }) {
            return Result.Error(DataError.Local.UNKNOWN)
        }

        val userPreferences = getUserPreferences()
            .onError { return it }
            .data()

        collectionRepository.deleteCollections(collectionIds.asList())
            .onError { return it }

        return saveUserPreferences(
            userPreferences.copy(
                collectionOrder = userPreferences.collectionOrder - collectionIds
                    .filter { it in userPreferences.collectionOrder }
            )
        )
    }
}

package hu.piware.bricklog.feature.collection.domain.usecase

import hu.piware.bricklog.feature.collection.domain.model.Collection
import hu.piware.bricklog.feature.collection.domain.model.isNew
import hu.piware.bricklog.feature.collection.domain.repository.CollectionRepository
import hu.piware.bricklog.feature.collection.domain.util.DefaultCollections
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.core.domain.data
import hu.piware.bricklog.feature.core.domain.onError
import hu.piware.bricklog.feature.user.domain.manager.SessionManager
import hu.piware.bricklog.feature.user.domain.model.UserId
import hu.piware.bricklog.feature.user.domain.usecase.GetUserPreferences
import hu.piware.bricklog.feature.user.domain.usecase.SaveUserPreferences
import org.koin.core.annotation.Single

@Single
class DeleteCollections(
    private val collectionRepository: CollectionRepository,
    private val getUserPreferences: GetUserPreferences,
    private val saveUserPreferences: SaveUserPreferences,
    private val sessionManager: SessionManager,
) {
    suspend operator fun invoke(
        vararg collections: Collection,
        userId: UserId = sessionManager.currentUserId,
    ): EmptyResult<DataError> {
        if (collections.any { it.isNew || DefaultCollections.entries.any { default -> it.type == default.type } }) {
            return Result.Error(DataError.Local.UNKNOWN)
        }

        val collectionIds = collections.map { it.id }

        collectionRepository.deleteCollections(userId, collectionIds)
            .onError { return it }

        val userPreferences = getUserPreferences(userId)
            .onError { return it }
            .data()

        return saveUserPreferences(
            userId = userId,
            preferences = userPreferences.copy(
                collectionOrder = userPreferences.collectionOrder - collectionIds
                    .filter { it in userPreferences.collectionOrder },
            ),
        )
    }
}

package hu.piware.bricklog.feature.collection.domain.usecase

import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.data
import hu.piware.bricklog.feature.core.domain.onError
import hu.piware.bricklog.feature.user.domain.manager.SessionManager
import hu.piware.bricklog.feature.user.domain.model.UserId
import hu.piware.bricklog.feature.user.domain.usecase.GetUserPreferences
import hu.piware.bricklog.feature.user.domain.usecase.SaveUserPreferences
import org.koin.core.annotation.Single

@Single
class SaveCollectionOrder(
    private val getUserPreferences: GetUserPreferences,
    private val saveUserPreferences: SaveUserPreferences,
    private val sessionManager: SessionManager,
) {
    suspend operator fun invoke(
        collectionIds: List<CollectionId>,
        userId: UserId = sessionManager.currentUserId,
    ): EmptyResult<DataError> {
        val userPreferences = getUserPreferences(userId)
            .onError { return it }
            .data()

        return saveUserPreferences(
            preferences = userPreferences.copy(
                collectionOrder = collectionIds,
            ),
            userId = userId,
        )
    }
}

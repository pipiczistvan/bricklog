package hu.piware.bricklog.feature.collection.domain.usecase

import hu.piware.bricklog.feature.collection.domain.model.Collection
import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.collection.domain.repository.CollectionRepository
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.core.domain.data
import hu.piware.bricklog.feature.core.domain.onError
import hu.piware.bricklog.feature.user.domain.manager.SessionManager
import hu.piware.bricklog.feature.user.domain.model.UserId
import hu.piware.bricklog.feature.user.domain.usecase.GetUserPreferences
import hu.piware.bricklog.feature.user.domain.usecase.SaveUserPreferences
import org.koin.core.annotation.Single

@Single
class SaveCollections(
    private val collectionRepository: CollectionRepository,
    private val getUserPreferences: GetUserPreferences,
    private val saveUserPreferences: SaveUserPreferences,
    private val getCollections: GetCollections,
    private val sessionManager: SessionManager,
) {
    suspend operator fun invoke(
        vararg collections: Collection,
        userId: UserId = sessionManager.currentUserId,
    ): Result<List<CollectionId>, DataError> {
        val userPreferences = getUserPreferences(userId)
            .onError { return it }
            .data()

        collectionRepository.saveCollections(userId, collections.asList())
            .onError { return it }

        val savedCollections = getCollections(userId)
            .onError { return it }
            .data()
            .map { it.id }

        saveUserPreferences(
            userId = userId,
            preferences = userPreferences.copy(
                collectionOrder = userPreferences.collectionOrder.mergeWith(savedCollections),
            ),
        ).onError { return it }

        return Result.Success(savedCollections)
    }
}

private fun List<CollectionId>.mergeWith(other: List<CollectionId>): List<CollectionId> {
    return (this + other.filter { it !in this }).filterNot { it.isEmpty() }
}

package hu.piware.bricklog.feature.collection.domain.usecase

import co.touchlab.kermit.Logger
import hu.piware.bricklog.feature.collection.domain.model.Collection
import hu.piware.bricklog.feature.collection.domain.repository.CollectionRepository
import hu.piware.bricklog.feature.collection.domain.util.DefaultCollections
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.core.domain.asEmptyDataResult
import hu.piware.bricklog.feature.core.domain.data
import hu.piware.bricklog.feature.core.domain.onError
import hu.piware.bricklog.feature.user.domain.manager.SessionManager
import hu.piware.bricklog.feature.user.domain.manager.isAuthenticated
import hu.piware.bricklog.feature.user.domain.model.UserId
import org.koin.core.annotation.Single

@Single
class InitializeDefaultCollections(
    private val getCollections: GetCollections,
    private val saveCollections: SaveCollections,
    private val sessionManager: SessionManager,
    private val collectionRepository: CollectionRepository,
) {
    private val logger = Logger.withTag("InitializeDefaultCollections")

    suspend operator fun invoke(userId: UserId = sessionManager.currentUserId): EmptyResult<DataError> {
        if (userId.isAuthenticated) {
            collectionRepository.forceSyncRemoteCollections(userId)
                .onError { return it }
        }

        val collections = getCollections(userId)
            .onError { return it }
            .data()

        val missingCollections = DefaultCollections.entries
            .filter { default -> collections.none { it.type == default.type } }
            .map { default ->
                Collection(
                    owner = userId,
                    name = default.collectionName,
                    icon = default.icon,
                    type = default.type,
                    shares = emptyMap(),
                )
            }

        if (missingCollections.isNotEmpty()) {
            logger.d { "Saving ${missingCollections.size} default collections for user $userId" }
            return saveCollections(
                collections = missingCollections.toTypedArray(),
                userId = userId,
            ).asEmptyDataResult()
        } else {
            logger.d { "No default collections to save for user $userId" }
            return Result.Success(Unit)
        }
    }
}

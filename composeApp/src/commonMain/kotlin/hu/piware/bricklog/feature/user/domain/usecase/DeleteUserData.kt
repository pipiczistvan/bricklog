package hu.piware.bricklog.feature.user.domain.usecase

import hu.piware.bricklog.feature.core.domain.AccountSyncedRepository
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.UserError
import hu.piware.bricklog.feature.user.domain.manager.SessionManager
import hu.piware.bricklog.feature.user.domain.model.UserId
import hu.piware.bricklog.feature.user.domain.repository.UserRepository

class DeleteUserData(
    private val userRepository: UserRepository,
    private val syncedRepositories: List<AccountSyncedRepository>,
    private val sessionManager: SessionManager,
) {
    suspend operator fun invoke(userId: UserId = sessionManager.currentUserId): EmptyResult<UserError> {
        syncedRepositories.forEach {
            it.clearLocalData(userId)
        }

        return userRepository.deleteUser()
    }
}

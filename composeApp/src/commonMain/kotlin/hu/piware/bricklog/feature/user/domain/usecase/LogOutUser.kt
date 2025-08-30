package hu.piware.bricklog.feature.user.domain.usecase

import hu.piware.bricklog.feature.core.domain.AccountSyncedRepository
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.UserError
import hu.piware.bricklog.feature.core.domain.onSuccess
import hu.piware.bricklog.feature.user.domain.manager.SessionManager
import hu.piware.bricklog.feature.user.domain.repository.UserRepository

class LogOutUser(
    private val userRepository: UserRepository,
    private val syncedRepositories: List<AccountSyncedRepository>,
    private val sessionManager: SessionManager,
) {
    suspend operator fun invoke(): EmptyResult<UserError> {
        val currentUserId = sessionManager.currentUserId

        return userRepository.logout()
            .onSuccess {
                syncedRepositories.forEach {
                    it.clearData(currentUserId)
                }
            }
    }
}

package hu.piware.bricklog.feature.user.domain.usecase

import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.SyncedRepository
import hu.piware.bricklog.feature.core.domain.UserError
import hu.piware.bricklog.feature.user.domain.repository.UserRepository

class DeleteUserData(
    private val userRepository: UserRepository,
    private val syncedRepositories: List<SyncedRepository>,
) {
    suspend operator fun invoke(): EmptyResult<UserError> {
        syncedRepositories.forEach {
            it.clearLocal()
            it.clearRemote()
        }

        return userRepository.deleteUser()
    }
}

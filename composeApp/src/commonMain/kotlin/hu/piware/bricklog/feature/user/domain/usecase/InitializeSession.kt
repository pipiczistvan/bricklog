package hu.piware.bricklog.feature.user.domain.usecase

import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.UserError
import hu.piware.bricklog.feature.user.domain.repository.UserRepository
import org.koin.core.annotation.Single

@Single
class InitializeSession(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(): EmptyResult<UserError.General> {
        return userRepository.initialize()
    }
}

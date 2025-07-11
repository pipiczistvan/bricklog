package hu.piware.bricklog.feature.user.domain.usecase

import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.UserError
import hu.piware.bricklog.feature.user.domain.repository.UserRepository
import org.koin.core.annotation.Single

@Single
class LogOutUser(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(): EmptyResult<UserError> {
        return userRepository.logout()
    }
}

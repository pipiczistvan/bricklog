package hu.piware.bricklog.feature.user.domain.usecase

import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.core.domain.UserError
import hu.piware.bricklog.feature.user.domain.repository.UserRepository
import hu.piware.bricklog.feature.user.presentation.util.isValidEmail
import org.koin.core.annotation.Single

@Single
class ResetPassword(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(email: String): EmptyResult<UserError> {
        if (!isValidEmail(email)) {
            return Result.Error(UserError.Login.INVALID_CREDENTIALS)
        }

        return userRepository.passwordReset(email)
    }
}

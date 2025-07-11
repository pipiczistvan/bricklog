package hu.piware.bricklog.feature.authentication.domain.usecase

import hu.piware.bricklog.feature.authentication.domain.repository.AuthenticationRepository
import hu.piware.bricklog.feature.authentication.presentation.util.isValidEmail
import hu.piware.bricklog.feature.core.domain.AuthenticationError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import org.koin.core.annotation.Single

@Single
class ResetPassword(
    private val authenticationRepository: AuthenticationRepository,
) {
    suspend operator fun invoke(email: String): EmptyResult<AuthenticationError> {
        if (!isValidEmail(email)) {
            return Result.Error(AuthenticationError.Login.INVALID_CREDENTIALS)
        }

        return authenticationRepository.passwordReset(email)
    }
}

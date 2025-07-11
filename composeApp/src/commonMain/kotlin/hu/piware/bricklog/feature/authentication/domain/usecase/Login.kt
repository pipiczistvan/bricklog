package hu.piware.bricklog.feature.authentication.domain.usecase

import hu.piware.bricklog.feature.authentication.domain.model.AuthenticationMethod
import hu.piware.bricklog.feature.authentication.domain.model.User
import hu.piware.bricklog.feature.authentication.domain.repository.AuthenticationRepository
import hu.piware.bricklog.feature.authentication.presentation.util.isValidEmail
import hu.piware.bricklog.feature.core.domain.AuthenticationError
import hu.piware.bricklog.feature.core.domain.Result
import org.koin.core.annotation.Single

@Single
class Login(
    private val authenticationRepository: AuthenticationRepository,
) {
    suspend operator fun invoke(method: AuthenticationMethod): Result<User?, AuthenticationError> {
        when (method) {
            is AuthenticationMethod.EmailPassword -> {
                if (!isValidEmail(method.email) || method.password.isBlank()) {
                    return Result.Error(AuthenticationError.Login.INVALID_CREDENTIALS)
                }
            }

            is AuthenticationMethod.GoogleSignIn -> {
                if (method.result.isFailure || method.result.getOrNull() == null) {
                    return Result.Error(AuthenticationError.Login.UNKNOWN)
                }
            }
        }

        return authenticationRepository.login(method)
    }
}

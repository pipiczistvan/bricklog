package hu.piware.bricklog.feature.user.domain.usecase

import hu.piware.bricklog.feature.core.domain.AuthenticationError
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.user.domain.model.AuthenticationMethod
import hu.piware.bricklog.feature.user.domain.model.User
import hu.piware.bricklog.feature.user.domain.repository.UserRepository
import hu.piware.bricklog.feature.user.presentation.util.isValidEmail
import org.koin.core.annotation.Single

@Single
class LogInUser(
    private val userRepository: UserRepository,
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

        return userRepository.login(method)
    }
}

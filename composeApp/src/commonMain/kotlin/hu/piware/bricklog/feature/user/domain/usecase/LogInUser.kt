package hu.piware.bricklog.feature.user.domain.usecase

import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.core.domain.UserError
import hu.piware.bricklog.feature.user.domain.model.AuthenticationMethod
import hu.piware.bricklog.feature.user.domain.model.User
import hu.piware.bricklog.feature.user.domain.repository.UserRepository
import hu.piware.bricklog.feature.user.presentation.util.isValidEmail
import org.koin.core.annotation.Single

@Single
class LogInUser(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(method: AuthenticationMethod): Result<User?, UserError> {
        when (method) {
            is AuthenticationMethod.EmailPassword -> {
                if (!isValidEmail(method.email) || method.password.isBlank()) {
                    return Result.Error(UserError.Login.INVALID_CREDENTIALS)
                }
            }

            is AuthenticationMethod.GoogleSignIn -> Unit
        }

        return userRepository.login(method)
    }
}

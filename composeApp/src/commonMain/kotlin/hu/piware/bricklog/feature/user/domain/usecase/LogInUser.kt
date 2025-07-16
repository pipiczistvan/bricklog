package hu.piware.bricklog.feature.user.domain.usecase

import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.core.domain.SyncedRepository
import hu.piware.bricklog.feature.core.domain.UserError
import hu.piware.bricklog.feature.user.domain.model.AuthenticationMethod
import hu.piware.bricklog.feature.user.domain.model.User
import hu.piware.bricklog.feature.user.domain.repository.UserRepository
import hu.piware.bricklog.feature.user.presentation.util.isValidEmail

class LogInUser(
    private val userRepository: UserRepository,
    private val syncedRepositories: List<SyncedRepository>,
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

        syncedRepositories.forEach {
            it.clearLocal()
        }

        return userRepository.login(method)
    }
}

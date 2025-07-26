package hu.piware.bricklog.feature.user.domain.usecase

import hu.piware.bricklog.feature.collection.domain.usecase.InitializeDefaultCollections
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.core.domain.UserError
import hu.piware.bricklog.feature.core.domain.onSuccess
import hu.piware.bricklog.feature.user.domain.model.AuthenticationMethod
import hu.piware.bricklog.feature.user.domain.model.User
import hu.piware.bricklog.feature.user.domain.repository.UserRepository
import hu.piware.bricklog.feature.user.presentation.util.isValidEmail
import hu.piware.bricklog.feature.user.presentation.util.isValidPassword
import org.koin.core.annotation.Single

@Single
class RegisterUser(
    private val userRepository: UserRepository,
    private val initializeDefaultCollections: InitializeDefaultCollections,
) {
    suspend operator fun invoke(method: AuthenticationMethod): Result<User?, UserError> {
        when (method) {
            is AuthenticationMethod.EmailPassword -> {
                if (!isValidEmail(method.email) || !isValidPassword(method.password)) {
                    return Result.Error(UserError.Register.INVALID_CREDENTIALS)
                }
            }

            is AuthenticationMethod.GoogleSignIn -> {
                return Result.Error(UserError.Register.UNKNOWN)
            }
        }

        return userRepository.register(method)
            .onSuccess {
                initializeDefaultCollections()
            }
    }
}

package hu.piware.bricklog.feature.user.domain.usecase

import hu.piware.bricklog.feature.collection.domain.usecase.InitializeDefaultCollections
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.core.domain.UserError
import hu.piware.bricklog.feature.core.domain.data
import hu.piware.bricklog.feature.core.domain.onError
import hu.piware.bricklog.feature.core.domain.onSuccess
import hu.piware.bricklog.feature.user.domain.model.AuthenticationMethod
import hu.piware.bricklog.feature.user.domain.model.User
import hu.piware.bricklog.feature.user.domain.repository.UserRepository
import hu.piware.bricklog.feature.user.presentation.util.isValidEmail
import org.koin.core.annotation.Single

@Single
class LogInUser(
    private val userRepository: UserRepository,
    private val initializeDefaultCollections: InitializeDefaultCollections,
    private val getUserPreferences: GetUserPreferences,
    private val saveUserPreferences: SaveUserPreferences,
) {
    suspend operator fun invoke(method: AuthenticationMethod): Result<User?, UserError> {
        when (method) {
            is AuthenticationMethod.EmailPassword -> {
                if (!isValidEmail(method.email) || method.password.isBlank()) {
                    return Result.Error(UserError.Login.INVALID_CREDENTIALS)
                }
            }

            is AuthenticationMethod.GoogleSignIn -> {
                if (method.googleUser?.idToken == null || method.googleUser.email == null) {
                    return Result.Error(UserError.Login.INVALID_CREDENTIALS)
                }
            }
        }

        val user = userRepository.login(method)
            .onError { return it }
            .data()

        if (user == null) {
            return Result.Error(UserError.Login.UNKNOWN)
        }

        initializeDefaultCollections()

        getUserPreferences()
            .onSuccess { userPreferences ->
                if (userPreferences.displayName?.isBlank() == true) {
                    saveUserPreferences(
                        userPreferences.copy(
                            displayName = user.name,
                        ),
                    )
                }
            }

        return Result.Success(user)
    }
}

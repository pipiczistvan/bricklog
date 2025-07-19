package hu.piware.bricklog.feature.user.domain.usecase

import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.core.domain.SyncedRepository
import hu.piware.bricklog.feature.core.domain.UserError
import hu.piware.bricklog.feature.core.domain.onSuccess
import hu.piware.bricklog.feature.settings.domain.model.UserPreferences
import hu.piware.bricklog.feature.settings.domain.repository.SettingsRepository
import hu.piware.bricklog.feature.user.domain.model.AuthenticationMethod
import hu.piware.bricklog.feature.user.domain.model.User
import hu.piware.bricklog.feature.user.domain.repository.UserRepository
import hu.piware.bricklog.feature.user.presentation.util.isValidEmail
import hu.piware.bricklog.feature.user.presentation.util.isValidPassword
import kotlinx.coroutines.flow.firstOrNull

class RegisterUser(
    private val userRepository: UserRepository,
    private val settingsRepository: SettingsRepository,
    private val syncedRepositories: List<SyncedRepository>,
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
        syncedRepositories.forEach {
            it.clearLocal()
        }

        return userRepository.register(method)
            .onSuccess {
                val currentPreferences = settingsRepository.userPreferences.firstOrNull()
                settingsRepository.saveUserPreferences(
                    (currentPreferences ?: UserPreferences()).copy(showGreetings = true)
                )
            }
    }
}

package hu.piware.bricklog.feature.authentication.domain.usecase

import hu.piware.bricklog.feature.authentication.domain.repository.AuthenticationRepository
import org.koin.core.annotation.Single

@Single
class WatchCurrentUser(
    private val authenticationRepository: AuthenticationRepository,
) {
    operator fun invoke() = authenticationRepository.currentUser
}

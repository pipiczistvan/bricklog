package hu.piware.bricklog.feature.user.domain.usecase

import hu.piware.bricklog.feature.user.domain.repository.UserRepository
import org.koin.core.annotation.Single

@Single
class WatchCurrentUser(
    private val userRepository: UserRepository,
) {
    operator fun invoke() = userRepository.currentUser
}

package hu.piware.bricklog.feature.authentication.domain.usecase

import hu.piware.bricklog.feature.authentication.domain.repository.AuthenticationRepository
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import org.koin.core.annotation.Single

@Single
class Logout(
    private val authenticationRepository: AuthenticationRepository,
) {
    suspend operator fun invoke(): EmptyResult<DataError> {
        return authenticationRepository.logout()
    }
}

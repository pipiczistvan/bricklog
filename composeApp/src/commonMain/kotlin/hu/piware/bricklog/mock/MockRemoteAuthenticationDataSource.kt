package hu.piware.bricklog.mock

import hu.piware.bricklog.feature.authentication.domain.datasource.RemoteAuthenticationDataSource
import hu.piware.bricklog.feature.authentication.domain.model.AuthenticationMethod
import hu.piware.bricklog.feature.authentication.domain.model.User
import hu.piware.bricklog.feature.core.domain.AuthenticationError
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import kotlinx.coroutines.delay

class MockRemoteAuthenticationDataSource : RemoteAuthenticationDataSource {

    private var currentUser: User? = null

    override suspend fun getCurrentUser(): User? {
        return currentUser
    }

    override suspend fun login(method: AuthenticationMethod): Result<User?, AuthenticationError.Login> {
        delay(1000)
        currentUser = MOCK_USER
        return Result.Success(currentUser)
    }

    override suspend fun register(method: AuthenticationMethod): Result<User?, AuthenticationError.Register> {
        delay(1000)
        currentUser = MOCK_USER
        return Result.Success(currentUser)
    }

    override suspend fun logout(): Result<User?, DataError.Remote> {
        currentUser = null
        return Result.Success(currentUser)
    }

    override suspend fun passwordReset(email: String): EmptyResult<AuthenticationError.Login> {
        return Result.Success(Unit)
    }

    companion object {
        private val MOCK_USER = User("mock")
    }
}

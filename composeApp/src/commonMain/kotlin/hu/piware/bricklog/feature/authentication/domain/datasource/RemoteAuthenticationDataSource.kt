package hu.piware.bricklog.feature.authentication.domain.datasource

import hu.piware.bricklog.feature.authentication.domain.model.AuthenticationMethod
import hu.piware.bricklog.feature.authentication.domain.model.User
import hu.piware.bricklog.feature.core.domain.AuthenticationError
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result

interface RemoteAuthenticationDataSource {
    suspend fun getCurrentUser(): User?
    suspend fun login(method: AuthenticationMethod): Result<User?, AuthenticationError.Login>
    suspend fun register(method: AuthenticationMethod): Result<User?, AuthenticationError.Register>
    suspend fun logout(): Result<User?, DataError.Remote>
    suspend fun passwordReset(email: String): EmptyResult<AuthenticationError.Login>
}

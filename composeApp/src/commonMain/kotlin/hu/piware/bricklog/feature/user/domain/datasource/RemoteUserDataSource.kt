package hu.piware.bricklog.feature.user.domain.datasource

import hu.piware.bricklog.feature.core.domain.AuthenticationError
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.user.domain.model.AuthenticationMethod
import hu.piware.bricklog.feature.user.domain.model.User

interface RemoteUserDataSource {
    suspend fun getCurrentUser(): User?
    suspend fun login(method: AuthenticationMethod): Result<User?, AuthenticationError.Login>
    suspend fun register(method: AuthenticationMethod): Result<User?, AuthenticationError.Register>
    suspend fun logout(): Result<User?, DataError.Remote>
    suspend fun passwordReset(email: String): EmptyResult<AuthenticationError.Login>
}

package hu.piware.bricklog.feature.authentication.domain.repository

import hu.piware.bricklog.feature.authentication.domain.model.AuthenticationMethod
import hu.piware.bricklog.feature.authentication.domain.model.User
import hu.piware.bricklog.feature.core.domain.AuthenticationError
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {
    val currentUser: Flow<User?>

    suspend fun login(method: AuthenticationMethod): Result<User?, AuthenticationError.Login>
    suspend fun register(method: AuthenticationMethod): Result<User?, AuthenticationError.Register>
    suspend fun logout(): EmptyResult<DataError.Remote>
    suspend fun passwordReset(email: String): EmptyResult<AuthenticationError.Login>
}

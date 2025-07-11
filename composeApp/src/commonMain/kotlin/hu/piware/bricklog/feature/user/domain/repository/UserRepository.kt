package hu.piware.bricklog.feature.user.domain.repository

import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.core.domain.UserError
import hu.piware.bricklog.feature.user.domain.model.AuthenticationMethod
import hu.piware.bricklog.feature.user.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    val currentUser: Flow<User?>

    suspend fun login(method: AuthenticationMethod): Result<User?, UserError.Login>
    suspend fun register(method: AuthenticationMethod): Result<User?, UserError.Register>
    suspend fun logout(): EmptyResult<UserError.General>
    suspend fun passwordReset(email: String): EmptyResult<UserError.General>
    suspend fun deleteUser(): EmptyResult<UserError.General>
}

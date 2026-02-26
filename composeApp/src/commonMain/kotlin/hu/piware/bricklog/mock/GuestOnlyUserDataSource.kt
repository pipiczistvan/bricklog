package hu.piware.bricklog.mock

import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.core.domain.UserError
import hu.piware.bricklog.feature.user.domain.datasource.RemoteUserDataSource
import hu.piware.bricklog.feature.user.domain.model.AuthenticationMethod
import hu.piware.bricklog.feature.user.domain.model.User

class GuestOnlyUserDataSource : RemoteUserDataSource {

    override suspend fun getCurrentUser(): User? {
        return null
    }

    override suspend fun login(method: AuthenticationMethod): Result<User?, UserError.Login> {
        throw NotImplementedError()
    }

    override suspend fun register(method: AuthenticationMethod): Result<User?, UserError.Register> {
        throw NotImplementedError()
    }

    override suspend fun logout(): Result<User?, UserError.General> {
        throw NotImplementedError()
    }

    override suspend fun passwordReset(email: String): EmptyResult<UserError.General> {
        throw NotImplementedError()
    }

    override suspend fun deleteUser(): Result<User?, UserError.General> {
        throw NotImplementedError()
    }
}
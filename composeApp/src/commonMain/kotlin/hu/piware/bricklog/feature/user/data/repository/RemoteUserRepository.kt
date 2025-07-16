package hu.piware.bricklog.feature.user.data.repository

import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.core.domain.UserError
import hu.piware.bricklog.feature.core.domain.asEmptyDataResult
import hu.piware.bricklog.feature.core.domain.onSuccess
import hu.piware.bricklog.feature.user.domain.datasource.RemoteUserDataSource
import hu.piware.bricklog.feature.user.domain.manager.SessionManager
import hu.piware.bricklog.feature.user.domain.model.AuthenticationMethod
import hu.piware.bricklog.feature.user.domain.model.User
import hu.piware.bricklog.feature.user.domain.repository.UserRepository
import org.koin.core.annotation.Single

@Single
class RemoteUserRepository(
    private val remoteDataSource: RemoteUserDataSource,
    private val sessionManager: SessionManager,
) : UserRepository {

    override suspend fun initialize(): EmptyResult<UserError.General> {
        val user = remoteDataSource.getCurrentUser()
        sessionManager.setCurrentUser(user)
        return Result.Success(Unit)
    }

    override suspend fun login(method: AuthenticationMethod): Result<User?, UserError.Login> {
        return remoteDataSource.login(method)
            .onSuccess(sessionManager::setCurrentUser)
    }

    override suspend fun register(method: AuthenticationMethod): Result<User?, UserError.Register> {
        return remoteDataSource.register(method)
            .onSuccess(sessionManager::setCurrentUser)
    }

    override suspend fun logout(): EmptyResult<UserError.General> {
        return remoteDataSource.logout()
            .onSuccess(sessionManager::setCurrentUser)
            .asEmptyDataResult()
    }

    override suspend fun passwordReset(email: String): EmptyResult<UserError.General> {
        return remoteDataSource.passwordReset(email)
    }

    override suspend fun deleteUser(): EmptyResult<UserError.General> {
        return remoteDataSource.deleteUser()
            .onSuccess(sessionManager::setCurrentUser)
            .asEmptyDataResult()
    }
}

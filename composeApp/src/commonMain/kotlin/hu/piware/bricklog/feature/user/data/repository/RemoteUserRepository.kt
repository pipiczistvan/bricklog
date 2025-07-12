package hu.piware.bricklog.feature.user.data.repository

import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.core.domain.UserError
import hu.piware.bricklog.feature.core.domain.asEmptyDataResult
import hu.piware.bricklog.feature.core.domain.onSuccess
import hu.piware.bricklog.feature.user.domain.datasource.RemoteUserDataSource
import hu.piware.bricklog.feature.user.domain.model.AuthenticationMethod
import hu.piware.bricklog.feature.user.domain.model.User
import hu.piware.bricklog.feature.user.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.Single

@Single
class RemoteUserRepository(
    private val remoteDataSource: RemoteUserDataSource,
    repositoryScope: CoroutineScope = CoroutineScope(Dispatchers.IO),
) : UserRepository {

    private var authenticatedUser = MutableStateFlow<User?>(null)

    init {
        repositoryScope.launch {
            val user = remoteDataSource.getCurrentUser()
            authenticatedUser.update { user }
        }
    }

    override val currentUser = authenticatedUser.asStateFlow()

    override suspend fun login(method: AuthenticationMethod): Result<User?, UserError.Login> {
        return remoteDataSource.login(method)
            .onSuccess { user -> authenticatedUser.update { user } }
    }

    override suspend fun register(method: AuthenticationMethod): Result<User?, UserError.Register> {
        return remoteDataSource.register(method)
            .onSuccess { user -> authenticatedUser.update { user } }
    }

    override suspend fun logout(): EmptyResult<UserError.General> {
        return remoteDataSource.logout()
            .onSuccess { user -> authenticatedUser.update { user } }
            .asEmptyDataResult()
    }

    override suspend fun passwordReset(email: String): EmptyResult<UserError.General> {
        return remoteDataSource.passwordReset(email)
    }

    override suspend fun deleteUser(): EmptyResult<UserError.General> {
        return remoteDataSource.deleteUser()
            .onSuccess { user -> authenticatedUser.update { user } }
            .asEmptyDataResult()
    }
}

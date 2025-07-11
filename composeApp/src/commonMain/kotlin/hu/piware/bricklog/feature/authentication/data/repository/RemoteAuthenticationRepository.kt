package hu.piware.bricklog.feature.authentication.data.repository

import hu.piware.bricklog.feature.authentication.domain.datasource.RemoteAuthenticationDataSource
import hu.piware.bricklog.feature.authentication.domain.model.AuthenticationMethod
import hu.piware.bricklog.feature.authentication.domain.model.User
import hu.piware.bricklog.feature.authentication.domain.repository.AuthenticationRepository
import hu.piware.bricklog.feature.core.domain.AuthenticationError
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.core.domain.asEmptyDataResult
import hu.piware.bricklog.feature.core.domain.onSuccess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.Single

@Single
class RemoteAuthenticationRepository(
    private val remoteDataSource: RemoteAuthenticationDataSource,
    repositoryScope: CoroutineScope = CoroutineScope(Dispatchers.IO),
) : AuthenticationRepository {

    private var authenticatedUser = MutableStateFlow<User?>(null)

    init {
        repositoryScope.launch {
            val user = remoteDataSource.getCurrentUser()
            authenticatedUser.update { user }
        }
    }

    override val currentUser = authenticatedUser.asStateFlow()

    override suspend fun login(method: AuthenticationMethod): Result<User?, AuthenticationError.Login> {
        return remoteDataSource.login(method)
            .onSuccess { user -> authenticatedUser.update { user } }
    }

    override suspend fun register(method: AuthenticationMethod): Result<User?, AuthenticationError.Register> {
        return remoteDataSource.register(method)
            .onSuccess { user -> authenticatedUser.update { user } }
    }

    override suspend fun logout(): EmptyResult<DataError.Remote> {
        return remoteDataSource.logout()
            .onSuccess { user -> authenticatedUser.update { user } }
            .asEmptyDataResult()
    }

    override suspend fun passwordReset(email: String): EmptyResult<AuthenticationError.Login> {
        return remoteDataSource.passwordReset(email)
    }
}

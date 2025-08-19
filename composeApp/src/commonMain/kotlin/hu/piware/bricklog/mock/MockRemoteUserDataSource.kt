package hu.piware.bricklog.mock

import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.core.domain.UserError
import hu.piware.bricklog.feature.user.domain.datasource.RemoteUserDataSource
import hu.piware.bricklog.feature.user.domain.model.AuthenticationMethod
import hu.piware.bricklog.feature.user.domain.model.User
import kotlinx.coroutines.delay

class MockRemoteUserDataSource : RemoteUserDataSource {

    private var registeredUser: User? = null
    private var currentUser: User? = null
    private val firestore = MockFirestore

    override suspend fun getCurrentUser(): User? {
        return currentUser
    }

    override suspend fun login(method: AuthenticationMethod): Result<User?, UserError.Login> {
        delay(1000)
        currentUser = registeredUser
        if (currentUser == null) {
            return Result.Error(UserError.Login.INVALID_CREDENTIALS)
        }
        return Result.Success(currentUser)
    }

    override suspend fun register(method: AuthenticationMethod): Result<User?, UserError.Register> {
        delay(1000)
        if (method !is AuthenticationMethod.EmailPassword) {
            return Result.Error(UserError.Register.UNKNOWN)
        }

        registeredUser = PreviewData.user
        currentUser = PreviewData.user
        return Result.Success(currentUser)
    }

    override suspend fun logout(): Result<User?, UserError.General> {
        currentUser = null
        return Result.Success(currentUser)
    }

    override suspend fun passwordReset(email: String): EmptyResult<UserError.General> {
        return Result.Success(Unit)
    }

    override suspend fun deleteUser(): Result<User?, UserError.General> {
        firestore.clear()
        return logout()
    }
}

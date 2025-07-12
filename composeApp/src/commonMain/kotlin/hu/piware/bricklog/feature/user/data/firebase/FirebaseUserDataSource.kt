package hu.piware.bricklog.feature.user.data.firebase

import co.touchlab.kermit.Logger
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuthInvalidCredentialsException
import dev.gitlive.firebase.auth.FirebaseAuthUserCollisionException
import dev.gitlive.firebase.auth.auth
import hu.piware.bricklog.feature.core.domain.AuthenticationError
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.core.domain.asEmptyDataResult
import hu.piware.bricklog.feature.user.domain.datasource.RemoteUserDataSource
import hu.piware.bricklog.feature.user.domain.model.AuthenticationMethod
import hu.piware.bricklog.feature.user.domain.model.User
import kotlinx.coroutines.CancellationException
import org.koin.core.annotation.Single

@Single
class FirebaseUserDataSource : RemoteUserDataSource {

    private val logger = Logger.withTag("FirebaseAuthenticationDataSource")
    private val auth = Firebase.auth

    override suspend fun getCurrentUser(): User? {
        return auth.currentUser?.toUser()
    }

    override suspend fun login(method: AuthenticationMethod): Result<User?, AuthenticationError.Login> {
        return when (method) {
            is AuthenticationMethod.EmailPassword -> {
                try {
                    val result = auth.signInWithEmailAndPassword(method.email, method.password)
                    logger.i { "Logged in successfully with email and password" }
                    Result.Success(result.user?.toUser())
                } catch (e: FirebaseAuthInvalidCredentialsException) {
                    logger.e(e) { "Invalid email or password" }
                    return Result.Error(AuthenticationError.Login.INVALID_CREDENTIALS)
                } catch (e: Exception) {
                    if (e is CancellationException) throw e
                    logger.e(e) { "An error occurred while logging in with email and password" }
                    return Result.Error(AuthenticationError.Login.UNKNOWN)
                }
            }

            is AuthenticationMethod.GoogleSignIn -> {
                if (method.result.isSuccess) {
                    Result.Success(method.result.getOrNull()?.toUser())
                } else {
                    Result.Error(AuthenticationError.Login.UNKNOWN)
                }
            }
        }
    }

    override suspend fun register(method: AuthenticationMethod): Result<User?, AuthenticationError.Register> {
        return when (method) {
            is AuthenticationMethod.EmailPassword -> {
                try {
                    val result = auth.createUserWithEmailAndPassword(method.email, method.password)
                    logger.i { "Registered successfully with email and password" }
                    return Result.Success(result.user?.toUser())
                } catch (e: FirebaseAuthInvalidCredentialsException) {
                    logger.e(e) { "Invalid email or password" }
                    return Result.Error(AuthenticationError.Register.INVALID_CREDENTIALS)
                } catch (e: FirebaseAuthUserCollisionException) {
                    logger.e(e) { "User already exists" }
                    return Result.Error(AuthenticationError.Register.USER_COLLISION)
                } catch (e: Exception) {
                    if (e is CancellationException) throw e
                    logger.e(e) { "An error occurred while registering with email and password" }
                    return Result.Error(AuthenticationError.Register.UNKNOWN)
                }
            }

            is AuthenticationMethod.GoogleSignIn -> {
                logger.e { "Google sign-in is not supported for registration" }
                Result.Error(AuthenticationError.Register.UNKNOWN)
            }
        }
    }

    override suspend fun logout(): Result<User?, DataError.Remote> {
        try {
            auth.signOut()
            logger.i { "Logged out successfully" }
            return Result.Success(getCurrentUser())
        } catch (e: Exception) {
            logger.e(e) { "An error occurred while logging out" }
            return Result.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun passwordReset(email: String): EmptyResult<AuthenticationError.Login> {
        try {
            auth.sendPasswordResetEmail(email)
            logger.i { "Password reset email sent" }
            return Result.Success(null).asEmptyDataResult()
        } catch (e: Exception) {
            logger.e(e) { "An error occurred while sending password reset email" }
            return Result.Error(AuthenticationError.Login.UNKNOWN)
        }
    }
}

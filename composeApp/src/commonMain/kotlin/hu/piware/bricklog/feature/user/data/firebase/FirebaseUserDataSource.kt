package hu.piware.bricklog.feature.user.data.firebase

import co.touchlab.kermit.Logger
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.EmailAuthProvider
import dev.gitlive.firebase.auth.FirebaseAuthInvalidCredentialsException
import dev.gitlive.firebase.auth.FirebaseAuthRecentLoginRequiredException
import dev.gitlive.firebase.auth.FirebaseAuthUserCollisionException
import dev.gitlive.firebase.auth.GoogleAuthProvider
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.core.domain.UserError
import hu.piware.bricklog.feature.core.domain.asEmptyDataResult
import hu.piware.bricklog.feature.user.domain.datasource.RemoteUserDataSource
import hu.piware.bricklog.feature.user.domain.model.AuthenticationMethod
import hu.piware.bricklog.feature.user.domain.model.User
import kotlinx.coroutines.CancellationException
import org.koin.core.annotation.Single

@Single
class FirebaseUserDataSource : RemoteUserDataSource {

    private val logger = Logger.withTag("FirebaseUserDataSource")

    private val firestore = Firebase.firestore
    private val auth = Firebase.auth

    override suspend fun getCurrentUser(): User? {
        return auth.currentUser?.toUser()
    }

    override suspend fun login(method: AuthenticationMethod): Result<User?, UserError.Login> {
        val credentials = when (method) {
            is AuthenticationMethod.EmailPassword -> {
                EmailAuthProvider.credential(method.email, method.password)
            }

            is AuthenticationMethod.GoogleSignIn -> {
                GoogleAuthProvider.credential(
                    method.googleUser?.idToken,
                    method.googleUser?.accessToken,
                )
            }
        }

        try {
            val result = auth.currentUser?.reauthenticateAndRetrieveData(credentials)
                ?: auth.signInWithCredential(credentials)
            logger.d { "Logged in successfully" }
            return Result.Success(result.user?.toUser())
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            logger.e(e) { "Invalid credentials" }
            return Result.Error(UserError.Login.INVALID_CREDENTIALS)
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            logger.e(e) { "An error occurred while logging in" }
            return Result.Error(UserError.Login.UNKNOWN)
        }
    }

    override suspend fun register(method: AuthenticationMethod): Result<User?, UserError.Register> {
        return when (method) {
            is AuthenticationMethod.EmailPassword -> {
                try {
                    val result = auth.createUserWithEmailAndPassword(method.email, method.password)
                    logger.d { "Registered successfully with email and password" }
                    return Result.Success(result.user?.toUser())
                } catch (e: FirebaseAuthInvalidCredentialsException) {
                    logger.e(e) { "Invalid email or password" }
                    return Result.Error(UserError.Register.INVALID_CREDENTIALS)
                } catch (e: FirebaseAuthUserCollisionException) {
                    logger.e(e) { "User already exists" }
                    return Result.Error(UserError.Register.USER_COLLISION)
                } catch (e: Exception) {
                    if (e is CancellationException) throw e
                    logger.e(e) { "An error occurred while registering with email and password" }
                    return Result.Error(UserError.Register.UNKNOWN)
                }
            }

            is AuthenticationMethod.GoogleSignIn -> {
                logger.e { "Google sign-in is not supported for registration" }
                Result.Error(UserError.Register.UNKNOWN)
            }
        }
    }

    override suspend fun logout(): Result<User?, UserError.General> {
        try {
            auth.signOut()
            logger.d { "Logged out successfully" }
            return Result.Success(getCurrentUser())
        } catch (e: Exception) {
            logger.e(e) { "An error occurred while logging out" }
            return Result.Error(UserError.General.UNKNOWN)
        }
    }

    override suspend fun passwordReset(email: String): EmptyResult<UserError.General> {
        try {
            auth.sendPasswordResetEmail(email)
            logger.d { "Password reset email sent" }
            return Result.Success(null).asEmptyDataResult()
        } catch (e: Exception) {
            logger.e(e) { "An error occurred while sending password reset email" }
            return Result.Error(UserError.General.UNKNOWN)
        }
    }

    override suspend fun deleteUser(): Result<User?, UserError.General> {
        try {
            val user =
                auth.currentUser ?: return Result.Error(UserError.General.REAUTHENTICATION_REQUIRED)
            firestore.document("user-data/${user.uid}").delete()
            logger.d { "User data deleted successfully" }
            user.delete()
            logger.d { "User deleted successfully" }
            return Result.Success(getCurrentUser())
        } catch (e: FirebaseAuthRecentLoginRequiredException) {
            logger.e(e) { "User must reauthenticate before deleting" }
            return Result.Error(UserError.General.REAUTHENTICATION_REQUIRED)
        } catch (e: Exception) {
            logger.e(e) { "An error occurred while deleting user" }
            return Result.Error(UserError.General.UNKNOWN)
        }
    }
}

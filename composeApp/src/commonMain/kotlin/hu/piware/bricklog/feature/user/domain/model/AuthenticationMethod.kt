package hu.piware.bricklog.feature.user.domain.model

import dev.gitlive.firebase.auth.FirebaseUser

sealed interface AuthenticationMethod {
    data class EmailPassword(
        val email: String,
        val password: String,
    ) : AuthenticationMethod

    data class GoogleSignIn(
        val result: Result<FirebaseUser?>,
    ) : AuthenticationMethod
}

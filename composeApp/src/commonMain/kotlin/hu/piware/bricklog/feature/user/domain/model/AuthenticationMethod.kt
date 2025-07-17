package hu.piware.bricklog.feature.user.domain.model

import com.mmk.kmpauth.google.GoogleUser

sealed interface AuthenticationMethod {
    data class EmailPassword(
        val email: String,
        val password: String,
    ) : AuthenticationMethod

    data class GoogleSignIn(
        val googleUser: GoogleUser?,
    ) : AuthenticationMethod
}

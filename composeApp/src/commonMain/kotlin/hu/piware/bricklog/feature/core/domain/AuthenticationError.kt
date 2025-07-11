package hu.piware.bricklog.feature.core.domain

sealed interface AuthenticationError : Error {
    enum class Login : AuthenticationError {
        INVALID_CREDENTIALS,
        UNKNOWN
    }

    enum class Register : AuthenticationError {
        INVALID_CREDENTIALS,
        USER_COLLISION,
        UNKNOWN
    }
}

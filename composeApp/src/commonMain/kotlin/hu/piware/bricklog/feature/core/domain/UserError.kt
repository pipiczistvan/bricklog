package hu.piware.bricklog.feature.core.domain

sealed interface UserError : Error {
    enum class General : UserError {
        REAUTHENTICATION_REQUIRED,
        UNKNOWN,
    }

    enum class Login : UserError {
        INVALID_CREDENTIALS,
        UNKNOWN,
    }

    enum class Register : UserError {
        INVALID_CREDENTIALS,
        USER_COLLISION,
        UNKNOWN,
    }
}

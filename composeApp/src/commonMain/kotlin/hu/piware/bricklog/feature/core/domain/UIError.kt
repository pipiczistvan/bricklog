package hu.piware.bricklog.feature.core.domain

sealed interface UIError : Error {
    enum class ValidationError : UIError {
        FIELD_BLANK,
        FIELD_TOO_SHORT,
        FIELD_TOO_LONG,
    }
}

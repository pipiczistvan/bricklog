package hu.piware.bricklog.feature.core.presentation

import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.core.domain.onError

suspend fun <T, E : DataError> Result<T, E>.showSnackbarOnError(): Result<T, E> {
    return onError { result ->
        SnackbarController.sendEvent(
            SnackbarEvent(
                message = result.error.toUiText().getAsString()
            )
        )
    }
}

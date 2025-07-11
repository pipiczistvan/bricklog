package hu.piware.bricklog.feature.core.presentation

import hu.piware.bricklog.feature.core.domain.Error
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.core.domain.onError
import hu.piware.bricklog.feature.core.domain.onSuccess
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString

suspend fun <T, E : Error> Result<T, E>.showSnackbarOnError(): Result<T, E> {
    return onError { result ->
        SnackbarController.sendEvent(
            SnackbarEvent(
                message = result.error.toUiText().getAsString()
            )
        )
    }
}

suspend fun <T, E : Error> Result<T, E>.showSnackbarOnSuccess(res: StringResource): Result<T, E> {
    return onSuccess {
        SnackbarController.sendEvent(
            SnackbarEvent(
                message = getString(res)
            )
        )
    }
}

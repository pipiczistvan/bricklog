package hu.piware.bricklog.feature.core.presentation

import hu.piware.bricklog.feature.core.domain.Error
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.core.domain.onError
import hu.piware.bricklog.feature.core.domain.onSuccess
import org.jetbrains.compose.resources.StringResource

suspend fun <T, E : Error> Result<T, E>.showSnackbarOnError(
    action: (E) -> SnackbarAction? = { null },
): Result<T, E> {
    return onError { result ->
        SnackbarController.sendEvent(
            SnackbarEvent(
                message = result.error.toUiText(),
                action = action(result.error),
            ),
        )
    }
}

suspend fun <T, E : Error> Result<T, E>.showSnackbarOnSuccess(res: StringResource): Result<T, E> {
    return onSuccess {
        SnackbarController.sendEvent(
            SnackbarEvent(
                message = UiText.StringResourceId(res),
            ),
        )
    }
}

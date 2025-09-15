package hu.piware.bricklog.feature.core.presentation

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

data class SnackbarEvent(
    val message: UiText,
    val action: SnackbarAction? = null,
)

data class SnackbarAction(
    val name: UiText,
    val action: () -> Unit,
)

object SnackbarEventController {

    private val _events = Channel<SnackbarEvent>()
    val events = _events.receiveAsFlow()

    suspend fun sendEvent(event: SnackbarEvent) {
        _events.send(event)
    }
}

@Composable
fun SnackbarEventHandler(
    snackbarHostState: SnackbarHostState,
) {
    val scope = rememberCoroutineScope()

    observeAsEvents(flow = SnackbarEventController.events, snackbarHostState) { event ->
        scope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()

            val result = snackbarHostState.showSnackbar(
                message = event.message.getAsString(),
                actionLabel = event.action?.name?.getAsString(),
                duration = SnackbarDuration.Long,
            )

            if (result == SnackbarResult.ActionPerformed) {
                event.action?.action?.invoke()
            }
        }
    }
}

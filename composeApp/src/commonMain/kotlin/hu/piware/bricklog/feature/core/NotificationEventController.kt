package hu.piware.bricklog.feature.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.mmk.kmpnotifier.notification.NotifierManager
import hu.piware.bricklog.feature.core.domain.AppEvent
import hu.piware.bricklog.feature.core.presentation.AppEventController
import hu.piware.bricklog.feature.core.presentation.SnackbarAction
import hu.piware.bricklog.feature.core.presentation.SnackbarEvent
import hu.piware.bricklog.feature.core.presentation.SnackbarEventController
import hu.piware.bricklog.feature.core.presentation.UiText
import hu.piware.bricklog.feature.core.presentation.observeAsEvents
import hu.piware.bricklog.util.AppState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

const val NOTIFICATION_EVENT_NEW_SETS = "new_sets"

sealed interface NotificationEvent {
    data class NewSets(
        val message: String,
        val firstAppearanceDateMs: Long,
    ) : NotificationEvent
}

object NotificationEventController {

    private val _events = Channel<NotificationEvent>()
    val events = _events.receiveAsFlow()

    suspend fun sendEvent(event: NotificationEvent) {
        _events.send(event)
    }
}

@Composable
fun NotificationEventHandler() {
    val scope = rememberCoroutineScope()

    observeAsEvents(flow = NotificationEventController.events) { event ->
        when (event) {
            is NotificationEvent.NewSets -> scope.launch {
                if (AppState.isAppInForeground()) {
                    SnackbarEventController.sendEvent(
                        SnackbarEvent(
                            message = UiText.DynamicString(event.message),
                            action = SnackbarAction(
                                name = UiText.DynamicString("Show"),
                                action = {
                                    scope.launch {
                                        AppEventController.sendEvent(AppEvent.ShowNewSets(event.firstAppearanceDateMs))
                                    }
                                },
                            ),
                        ),
                    )
                } else {
                    NotifierManager.getLocalNotifier().notify {
                        title = "New items"
                        body = event.message
                        payloadData = mapOf(
                            "type" to NOTIFICATION_EVENT_NEW_SETS,
                            "minAppearanceDateMs" to event.firstAppearanceDateMs.toString(),
                        )
                    }
                }
            }
        }
    }
}

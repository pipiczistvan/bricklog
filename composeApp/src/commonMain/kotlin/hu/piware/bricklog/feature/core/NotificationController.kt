package hu.piware.bricklog.feature.core

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

const val NOTIFICATION_EVENT_NEW_SETS = "new_sets"

sealed interface NotificationEvent {
    data object Empty : NotificationEvent
    data class NewSets(val startDate: Long) : NotificationEvent
}

object NotificationController {

    private val _events = Channel<NotificationEvent>()
    val events = _events.receiveAsFlow()

    suspend fun sendEvent(event: NotificationEvent) {
        _events.send(event)
    }
}

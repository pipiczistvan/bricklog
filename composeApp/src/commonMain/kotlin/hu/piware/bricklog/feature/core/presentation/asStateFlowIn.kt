package hu.piware.bricklog.feature.core.presentation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

fun <T> MutableStateFlow<T>.asStateFlowIn(
    scope: CoroutineScope,
    action: suspend FlowCollector<T>.() -> Unit,
): StateFlow<T> {
    return onStart(action)
        .stateIn(
            scope,
            SharingStarted.WhileSubscribed(5000L),
            value
        )
}

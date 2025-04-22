@file:OptIn(FlowPreview::class)

package hu.piware.bricklog.feature.core.presentation

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.withIndex

fun <T> Flow<T>.debounceAfterFirst(timeoutMillis: Long): Flow<T> =
    this
        .withIndex()
        .debounce { if (it.index == 0) 0 else timeoutMillis }
        .map { it.value }

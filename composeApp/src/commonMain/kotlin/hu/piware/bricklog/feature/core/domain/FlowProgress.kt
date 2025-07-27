package hu.piware.bricklog.feature.core.domain

import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.single

sealed class ValueOrProgress<V, P> {
    data class Value<V, P>(val value: V) : ValueOrProgress<V, P>()
    data class Progress<V, P>(val progress: P) : ValueOrProgress<V, P>()
}

typealias FlowForValue<V, P> = Flow<ValueOrProgress<V, P>>

suspend fun <V, P> FlowCollector<ValueOrProgress<V, P>>.emitProgress(progress: P) =
    emit(ValueOrProgress.Progress(progress))

suspend fun <V, P> FlowCollector<ValueOrProgress<V, P>>.emitValue(value: V) =
    emit(ValueOrProgress.Value(value))

suspend fun <V, P> SendChannel<ValueOrProgress<V, P>>.sendProgress(progress: P) =
    send(ValueOrProgress.Progress(progress))

suspend fun <V, P> SendChannel<ValueOrProgress<V, P>>.sendValue(value: V) =
    send(ValueOrProgress.Value(value))

fun <V, P> SendChannel<ValueOrProgress<V, P>>.trySendProgress(progress: P) =
    trySend(ValueOrProgress.Progress(progress))

fun <V, P> SendChannel<ValueOrProgress<V, P>>.trySendValue(value: V) =
    trySend(ValueOrProgress.Value(value))

suspend inline fun <V, P> FlowForValue<V, P>.collectForValue(crossinline action: suspend (value: P) -> Unit): V {
    return onEach {
        if (it is ValueOrProgress.Progress<V, P>) action(it.progress)
    }.filterIsInstance<ValueOrProgress.Value<V, P>>().single().value
}

suspend inline fun <V, P> FlowForValue<V, P>.collectDistinctForValue(
    crossinline areEquivalent: (old: P, new: P) -> Boolean,
    crossinline action: suspend (value: P) -> Unit,
): V {
    return distinctUntilChanged { old, new ->
        if (old is ValueOrProgress.Progress<V, P> && new is ValueOrProgress.Progress<V, P>) {
            areEquivalent(old.progress, new.progress)
        } else {
            old == new
        }
    }.collectForValue(action)
}

interface FlowProgressCollector<P> {
    suspend fun emitProgress(value: P)
}

suspend inline fun <V, P> FlowProgressCollector<P>.await(flowForValue: () -> FlowForValue<V, P>) =
    flowForValue().collectForValue {
        emitProgress(it)
    }

fun <V, P> flowForValue(work: suspend FlowProgressCollector<P>.() -> V): FlowForValue<V, P> {
    return channelFlow<ValueOrProgress<V, P>> {
        val collector = object : FlowProgressCollector<P> {
            override suspend fun emitProgress(value: P) {
                sendProgress(value)
            }
        }
        val value = collector.work()
        sendValue(value)
    }
}

typealias FlowForResult<V, P> = FlowForValue<Result<V, DataError>, P>

fun <V, P> flowForResult(work: suspend FlowProgressCollector<P>.() -> Result<V, DataError>) =
    flowForValue(work)

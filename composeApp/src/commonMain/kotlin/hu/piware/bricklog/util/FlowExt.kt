@file:OptIn(ExperimentalCoroutinesApi::class)

package hu.piware.bricklog.util

import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest

suspend fun <T> Flow<T>.firstOrDefault(default: () -> T): T {
    return firstOrNull() ?: default()
}

suspend fun <T> Flow<T?>.asResult(): Result<T, DataError> {
    return asResultOrDefault { throw NullPointerException() }
}

suspend fun <T> Flow<T>.asResultOrNull(): Result<T?, DataError> {
    return asResultOrDefault { null }
}

suspend fun <T> Flow<T?>.asResultOrDefault(default: () -> T): Result<T, DataError> {
    return try {
        Result.Success(firstOrNull() ?: default())
    } catch (_: Exception) {
        Result.Error(DataError.Local.UNKNOWN)
    }
}

fun <T, R> Flow<T>.flatMapLatestConditional(
    predicate: (T) -> Boolean,
    flow1: Flow<T>.() -> Flow<R>,
    flow2: Flow<T>.() -> Flow<R>,
) =
    flatMapLatest {
        if (predicate(it)) {
            flow1()
        } else {
            flow2()
        }
    }

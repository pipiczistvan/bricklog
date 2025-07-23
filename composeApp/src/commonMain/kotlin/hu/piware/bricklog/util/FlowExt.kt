package hu.piware.bricklog.util

import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

suspend fun <T> Flow<T>.firstOrDefault(default: () -> T): T {
    return firstOrNull() ?: default()
}

suspend fun <T> Flow<T>.asResult(): Result<T, DataError.Local> {
    return asResultOrDefault { throw NullPointerException() }
}

suspend fun <T> Flow<T>.asResultOrNull(): Result<T?, DataError.Local> {
    return asResultOrDefault { null }
}

suspend fun <T> Flow<T>.asResultOrDefault(default: () -> T): Result<T, DataError.Local> {
    return try {
        Result.Success(firstOrNull() ?: default())
    } catch (_: Exception) {
        Result.Error(DataError.Local.UNKNOWN)
    }
}

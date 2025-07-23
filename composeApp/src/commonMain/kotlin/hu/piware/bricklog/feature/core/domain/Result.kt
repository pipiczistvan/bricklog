package hu.piware.bricklog.feature.core.domain

sealed interface Result<out D, out E : Error> {
    data class Success<out D>(val data: D) : Result<D, Nothing>
    data class Error<out E : hu.piware.bricklog.feature.core.domain.Error>(val error: E) :
        Result<Nothing, E>
}

inline fun <T, E : Error, R> Result<T, E>.map(map: (T) -> R): Result<R, E> {
    return when (this) {
        is Result.Error -> Result.Error(error)
        is Result.Success -> Result.Success(map(data))
    }
}

fun <T, E : Error> Result<T, E>.asEmptyDataResult(): EmptyResult<E> {
    return map { }
}

inline fun <T, E : Error> Result<T, E>.onSuccess(action: (T) -> Unit): Result<T, E> {
    return when (this) {
        is Result.Error -> this
        is Result.Success -> {
            action(data)
            this
        }
    }
}

inline fun <T, E : Error> Result<T, E>.onError(action: (Result.Error<E>) -> Unit): Result<T, E> {
    return when (this) {
        is Result.Error -> {
            action(this)
            this
        }

        is Result.Success -> this
    }
}

fun <T, E : Error> Result<T, E>.data(): T {
    return when (this) {
        is Result.Error -> throw IllegalStateException("Result is error")
        is Result.Success -> data
    }
}

fun <T, E : Error> Result<Collection<T>, E>.first() = map { it.first() }

typealias EmptyResult<E> = Result<Unit, E>

package hu.piware.bricklog.feature.set.domain.usecase

import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.core.domain.UIError
import org.koin.core.annotation.Single

@Single
class ValidateUserName {

    operator fun invoke(name: String): EmptyResult<UIError> {
        if (name.length > MAX_LENGTH) {
            return Result.Error(UIError.ValidationError.FIELD_TOO_LONG)
        }

        return Result.Success(Unit)
    }

    companion object {
        const val MAX_LENGTH = 20
    }
}

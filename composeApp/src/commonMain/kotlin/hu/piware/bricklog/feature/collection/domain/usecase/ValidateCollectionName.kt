package hu.piware.bricklog.feature.collection.domain.usecase

import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.core.domain.UIError
import org.koin.core.annotation.Single

@Single
class ValidateCollectionName {

    operator fun invoke(name: String): EmptyResult<UIError> {
        if (name.isBlank()) {
            return Result.Error(UIError.ValidationError.FIELD_BLANK)
        }
        if (name.length > MAX_LENGTH) {
            return Result.Error(UIError.ValidationError.FIELD_TOO_LONG)
        }

        return Result.Success(Unit)
    }

    companion object {
        const val MAX_LENGTH = 20
    }
}

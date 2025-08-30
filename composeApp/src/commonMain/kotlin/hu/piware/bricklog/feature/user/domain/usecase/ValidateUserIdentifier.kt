package hu.piware.bricklog.feature.user.domain.usecase

import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.core.domain.UIError
import hu.piware.bricklog.feature.user.domain.model.UserId
import org.koin.core.annotation.Single

@Single
class ValidateUserIdentifier {

    operator fun invoke(userId: UserId): EmptyResult<UIError> {
        if (userId.isBlank()) {
            return Result.Error(UIError.ValidationError.FIELD_BLANK)
        }

        return Result.Success(Unit)
    }
}

package hu.piware.bricklog.feature.core.presentation

import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.error_busy
import bricklog.composeapp.generated.resources.error_disk_full
import bricklog.composeapp.generated.resources.error_field_blank
import bricklog.composeapp.generated.resources.error_field_too_long
import bricklog.composeapp.generated.resources.error_field_too_short
import bricklog.composeapp.generated.resources.error_invalid_credentials
import bricklog.composeapp.generated.resources.error_no_internet
import bricklog.composeapp.generated.resources.error_reauthentication_required
import bricklog.composeapp.generated.resources.error_request_timeout
import bricklog.composeapp.generated.resources.error_serialization
import bricklog.composeapp.generated.resources.error_too_many_requests
import bricklog.composeapp.generated.resources.error_unknown
import bricklog.composeapp.generated.resources.error_user_collision
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.Error
import hu.piware.bricklog.feature.core.domain.UIError
import hu.piware.bricklog.feature.core.domain.UserError

fun Error.toUiText(): UiText {
    return when (this) {
        is DataError -> toUiText(this)
        is UIError -> toUiText(this)
        is UserError -> toUiText(this)
    }
}

private fun toUiText(error: DataError): UiText {
    val stringRes = when (error) {
        DataError.Local.DISK_FULL -> Res.string.error_disk_full
        DataError.Local.BUSY -> Res.string.error_busy
        DataError.Local.UNKNOWN -> Res.string.error_unknown
        DataError.Remote.REQUEST_TIMEOUT -> Res.string.error_request_timeout
        DataError.Remote.TOO_MANY_REQUESTS -> Res.string.error_too_many_requests
        DataError.Remote.NO_INTERNET -> Res.string.error_no_internet
        DataError.Remote.SERVER -> Res.string.error_unknown
        DataError.Remote.SERIALIZATION -> Res.string.error_serialization
        DataError.Remote.UNKNOWN -> Res.string.error_unknown
    }

    return UiText.StringResourceId(stringRes)
}

private fun toUiText(error: UIError): UiText {
    val stringRes = when (error) {
        UIError.ValidationError.FIELD_BLANK -> Res.string.error_field_blank
        UIError.ValidationError.FIELD_TOO_LONG -> Res.string.error_field_too_long
        UIError.ValidationError.FIELD_TOO_SHORT -> Res.string.error_field_too_short
    }

    return UiText.StringResourceId(stringRes)
}

private fun toUiText(error: UserError): UiText {
    val stringRes = when (error) {
        UserError.Login.INVALID_CREDENTIALS,
        UserError.Register.INVALID_CREDENTIALS,
            -> Res.string.error_invalid_credentials

        UserError.Register.USER_COLLISION -> Res.string.error_user_collision
        UserError.Login.UNKNOWN,
        UserError.Register.UNKNOWN,
        UserError.General.UNKNOWN,
            -> Res.string.error_unknown

        UserError.General.REAUTHENTICATION_REQUIRED -> Res.string.error_reauthentication_required
    }

    return UiText.StringResourceId(stringRes)
}

package hu.piware.bricklog.feature.core.presentation

import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.error_busy
import bricklog.composeapp.generated.resources.error_disk_full
import bricklog.composeapp.generated.resources.error_field_blank
import bricklog.composeapp.generated.resources.error_field_too_long
import bricklog.composeapp.generated.resources.error_field_too_short
import bricklog.composeapp.generated.resources.error_no_internet
import bricklog.composeapp.generated.resources.error_request_timeout
import bricklog.composeapp.generated.resources.error_serialization
import bricklog.composeapp.generated.resources.error_too_many_requests
import bricklog.composeapp.generated.resources.error_unknown
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.UIError

fun DataError.toUiText(): UiText {
    val stringRes = when (this) {
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

fun UIError.toUiText(): UiText {
    val stringRes = when (this) {
        UIError.ValidationError.FIELD_BLANK -> Res.string.error_field_blank
        UIError.ValidationError.FIELD_TOO_LONG -> Res.string.error_field_too_long
        UIError.ValidationError.FIELD_TOO_SHORT -> Res.string.error_field_too_short
    }

    return UiText.StringResourceId(stringRes)
}

package hu.piware.bricklog.feature.set.domain.model

import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.status_filter_active
import bricklog.composeapp.generated.resources.status_filter_any_status
import bricklog.composeapp.generated.resources.status_filter_expired
import bricklog.composeapp.generated.resources.status_filter_future
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.StringResource

@Serializable
enum class StatusFilterOption(
    val titleRes: StringResource,
) {
    ANY_STATUS(Res.string.status_filter_any_status),
    ACTIVE(Res.string.status_filter_active),
    EXPIRED(Res.string.status_filter_expired),
    FUTURE(Res.string.status_filter_future),
}
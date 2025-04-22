package hu.piware.bricklog.feature.set.domain.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.set_status_active
import bricklog.composeapp.generated.resources.set_status_arrives_soon
import bricklog.composeapp.generated.resources.set_status_discontinued
import bricklog.composeapp.generated.resources.set_status_discontinued_soon
import bricklog.composeapp.generated.resources.set_status_future_release
import bricklog.composeapp.generated.resources.set_status_unknown
import hu.piware.bricklog.ui.theme.BricklogTheme
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.StringResource
import kotlin.time.Duration.Companion.days

data class SetUI(
    val set: Set,
    val isFavourite: Boolean,
    val status: SetStatus,
)

val SetUI.setID: Int
    get() = set.setID

val SetUI.EUPrice: Double?
    get() = set.DEPrice

val SetUI.localLaunchDate: LocalDateTime?
    get() = set.launchDate?.toLocalDateTime(TimeZone.currentSystemDefault())

val SetUI.localExitDate: LocalDateTime?
    get() = set.exitDate?.toLocalDateTime(TimeZone.currentSystemDefault())

fun Set.calculateStatus(): SetStatus {
    val now = Clock.System.now()

    return when {
        launchDate != null && now < launchDate && now + 60.days > launchDate -> SetStatus.ARRIVES_SOON
        launchDate != null && now < launchDate -> SetStatus.FUTURE_RELEASE
        launchDate != null && now >= launchDate && (exitDate == null || now < exitDate) -> SetStatus.ACTIVE
        exitDate != null && now < exitDate && now + 60.days > exitDate -> SetStatus.DISCONTINUED_SOON
        exitDate != null && now > exitDate -> SetStatus.DISCONTINUED
        else -> SetStatus.UNKNOWN
    }
}

enum class SetStatus(
    val statusRes: StringResource,
) {
    UNKNOWN(Res.string.set_status_unknown),
    FUTURE_RELEASE(Res.string.set_status_future_release),
    ARRIVES_SOON(Res.string.set_status_arrives_soon),
    ACTIVE(Res.string.set_status_active),
    DISCONTINUED_SOON(Res.string.set_status_discontinued_soon),
    DISCONTINUED(Res.string.set_status_discontinued),
}

val SetStatus.containerColor: Color
    @Composable
    @ReadOnlyComposable
    get() = when (this) {
        SetStatus.UNKNOWN -> Color.Gray
        SetStatus.FUTURE_RELEASE -> BricklogTheme.colorScheme.future.colorContainer
        SetStatus.ARRIVES_SOON -> BricklogTheme.colorScheme.futureVariant.colorContainer
        SetStatus.ACTIVE -> BricklogTheme.colorScheme.active.colorContainer
        SetStatus.DISCONTINUED_SOON -> BricklogTheme.colorScheme.discontinuedVariant.colorContainer
        SetStatus.DISCONTINUED -> BricklogTheme.colorScheme.discontinued.colorContainer
    }

val SetStatus.textColor: Color
    @Composable
    @ReadOnlyComposable
    get() = when (this) {
        SetStatus.UNKNOWN -> Color.Black
        SetStatus.FUTURE_RELEASE -> BricklogTheme.colorScheme.future.onColorContainer
        SetStatus.ARRIVES_SOON -> BricklogTheme.colorScheme.futureVariant.onColorContainer
        SetStatus.ACTIVE -> BricklogTheme.colorScheme.active.onColorContainer
        SetStatus.DISCONTINUED_SOON -> BricklogTheme.colorScheme.discontinuedVariant.onColorContainer
        SetStatus.DISCONTINUED -> BricklogTheme.colorScheme.discontinued.onColorContainer
    }

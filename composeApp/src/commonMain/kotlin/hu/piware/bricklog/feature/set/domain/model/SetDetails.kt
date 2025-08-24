package hu.piware.bricklog.feature.set.domain.model

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.feature_set_search_status_active
import bricklog.composeapp.generated.resources.feature_set_search_status_arrives_soon
import bricklog.composeapp.generated.resources.feature_set_search_status_future_release
import bricklog.composeapp.generated.resources.feature_set_search_status_retired
import bricklog.composeapp.generated.resources.feature_set_search_status_retired_soon
import bricklog.composeapp.generated.resources.feature_set_search_status_unknown
import hu.piware.bricklog.feature.collection.domain.model.Collection
import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.ui.theme.BricklogTheme
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.StringResource

data class SetDetails(
    val set: Set,
    val collections: List<Collection>,
    val status: SetStatus,
    val priceCategory: SetPriceCategory,
)

fun SetDetails.isInCollection(collectionId: CollectionId): Boolean {
    return collections.any { it.id == collectionId }
}

val SetDetails.setID: Int
    get() = set.setID

val SetDetails.setNumberWithVariant: String
    get() = "${set.number}${set.numberVariant?.let { "-$it" } ?: ""}"

val SetDetails.EUPrice: Double?
    get() = set.DEPrice

val SetDetails.USPrice: Double?
    get() = set.USPrice

val SetDetails.localLaunchDate: LocalDateTime?
    get() = set.launchDate?.toLocalDateTime(TimeZone.currentSystemDefault())

val SetDetails.localExitDate: LocalDateTime?
    get() = set.exitDate?.toLocalDateTime(TimeZone.currentSystemDefault())

const val FUTURE_DAYS_COUNT = 90

enum class SetStatus(
    val statusRes: StringResource,
) {
    UNKNOWN(Res.string.feature_set_search_status_unknown),
    FUTURE_RELEASE(Res.string.feature_set_search_status_future_release),
    ARRIVES_SOON(Res.string.feature_set_search_status_arrives_soon),
    ACTIVE(Res.string.feature_set_search_status_active),
    RETIRED_SOON(Res.string.feature_set_search_status_retired_soon),
    RETIRED(Res.string.feature_set_search_status_retired),
}

val SetStatus.containerColor: Color
    @Composable
    @ReadOnlyComposable
    get() = when (this) {
        SetStatus.UNKNOWN -> Color.Gray
        SetStatus.FUTURE_RELEASE -> BricklogTheme.colorScheme.purple.colorContainer
        SetStatus.ARRIVES_SOON -> BricklogTheme.colorScheme.blue.colorContainer
        SetStatus.ACTIVE -> BricklogTheme.colorScheme.green.colorContainer
        SetStatus.RETIRED_SOON -> BricklogTheme.colorScheme.orange.colorContainer
        SetStatus.RETIRED -> BricklogTheme.colorScheme.red.colorContainer
    }

val SetStatus.textColor: Color
    @Composable
    @ReadOnlyComposable
    get() = when (this) {
        SetStatus.UNKNOWN -> Color.Black
        SetStatus.FUTURE_RELEASE -> BricklogTheme.colorScheme.purple.onColorContainer
        SetStatus.ARRIVES_SOON -> BricklogTheme.colorScheme.blue.onColorContainer
        SetStatus.ACTIVE -> BricklogTheme.colorScheme.green.onColorContainer
        SetStatus.RETIRED_SOON -> BricklogTheme.colorScheme.orange.onColorContainer
        SetStatus.RETIRED -> BricklogTheme.colorScheme.red.onColorContainer
    }

enum class SetPriceCategory {
    BUDGET,
    AFFORDABLE,
    EXPENSIVE,
    PREMIUM,
    UNKNOWN,
}

val SetPriceCategory.containerColor: Color
    @Composable
    @ReadOnlyComposable
    get() = when (this) {
        SetPriceCategory.UNKNOWN -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
        SetPriceCategory.BUDGET -> BricklogTheme.colorScheme.green.colorContainer
        SetPriceCategory.AFFORDABLE -> BricklogTheme.colorScheme.yellow.colorContainer
        SetPriceCategory.EXPENSIVE -> BricklogTheme.colorScheme.orange.colorContainer
        SetPriceCategory.PREMIUM -> BricklogTheme.colorScheme.red.colorContainer
    }

val SetPriceCategory.textColor: Color
    @Composable
    @ReadOnlyComposable
    get() = when (this) {
        SetPriceCategory.UNKNOWN -> MaterialTheme.colorScheme.onBackground
        SetPriceCategory.BUDGET -> BricklogTheme.colorScheme.green.onColorContainer
        SetPriceCategory.AFFORDABLE -> BricklogTheme.colorScheme.yellow.onColorContainer
        SetPriceCategory.EXPENSIVE -> BricklogTheme.colorScheme.orange.onColorContainer
        SetPriceCategory.PREMIUM -> BricklogTheme.colorScheme.red.onColorContainer
    }

package hu.piware.bricklog.feature.set.domain.model

import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.search_filter_date_any_time
import bricklog.composeapp.generated.resources.search_filter_date_custom_range
import bricklog.composeapp.generated.resources.search_filter_date_one_month
import bricklog.composeapp.generated.resources.search_filter_date_one_week
import bricklog.composeapp.generated.resources.search_filter_date_one_year
import bricklog.composeapp.generated.resources.set_sort_appear_date_descending
import bricklog.composeapp.generated.resources.set_sort_launch_date_ascending
import bricklog.composeapp.generated.resources.set_sort_launch_date_descending
import bricklog.composeapp.generated.resources.set_sort_retiring_date_ascending
import bricklog.composeapp.generated.resources.set_sort_retiring_date_descending
import bricklog.composeapp.generated.resources.status_filter_active
import bricklog.composeapp.generated.resources.status_filter_any_status
import bricklog.composeapp.generated.resources.status_filter_expired
import bricklog.composeapp.generated.resources.status_filter_future
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.StringResource
import kotlin.collections.Set

@Serializable
data class SetFilter(
    val sortOption: SetSortOption? = null,
    val launchDate: DateFilter? = null,
    val themes: Set<String>? = null,
    val packagingTypes: Set<String>? = null,
    val status: StatusFilterOption? = null,
    val showIncomplete: Boolean? = null,
    val limit: Int? = null,
    val barcode: String? = null,
    val isFavourite: Boolean? = null,
)

@Serializable
enum class SetSortOption(
    val titleRes: StringResource,
) {
    LAUNCH_DATE_ASCENDING(
        titleRes = Res.string.set_sort_launch_date_ascending
    ),
    LAUNCH_DATE_DESCENDING(
        titleRes = Res.string.set_sort_launch_date_descending
    ),
    RETIRING_DATE_ASCENDING(
        titleRes = Res.string.set_sort_retiring_date_ascending
    ),
    RETIRING_DATE_DESCENDING(
        titleRes = Res.string.set_sort_retiring_date_descending
    ),
    APPEARANCE_DATE_DESCENDING(
        titleRes = Res.string.set_sort_appear_date_descending
    ),
}

@Serializable
sealed class DateFilter(
    val option: DateFilterOption,
) {
    @Serializable
    data object AnyTime : DateFilter(DateFilterOption.ANY_TIME)

    @Serializable
    data object OneWeek : DateFilter(DateFilterOption.ONE_WEEK)

    @Serializable
    data object OneMonth : DateFilter(DateFilterOption.ONE_MONTH)

    @Serializable
    data object OneYear : DateFilter(DateFilterOption.ONE_YEAR)

    @Serializable
    data class Custom(val startDate: Long, val endDate: Long) : DateFilter(DateFilterOption.CUSTOM)
}

enum class DateFilterOption(
    val titleRes: StringResource,
) {
    ANY_TIME(
        titleRes = Res.string.search_filter_date_any_time
    ),
    ONE_WEEK(
        titleRes = Res.string.search_filter_date_one_week
    ),
    ONE_MONTH(
        titleRes = Res.string.search_filter_date_one_month
    ),
    ONE_YEAR(
        titleRes = Res.string.search_filter_date_one_year
    ),
    CUSTOM(
        titleRes = Res.string.search_filter_date_custom_range
    )
}

@Serializable
enum class StatusFilterOption(
    val titleRes: StringResource,
) {
    ANY_STATUS(Res.string.status_filter_any_status),
    ACTIVE(Res.string.status_filter_active),
    EXPIRED(Res.string.status_filter_expired),
    FUTURE(Res.string.status_filter_future),
}

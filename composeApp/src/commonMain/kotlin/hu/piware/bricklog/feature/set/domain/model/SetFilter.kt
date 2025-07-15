package hu.piware.bricklog.feature.set.domain.model

import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.feature_set_search_date_filter_btn_any_time
import bricklog.composeapp.generated.resources.feature_set_search_date_filter_btn_custom_range
import bricklog.composeapp.generated.resources.feature_set_search_date_filter_btn_one_month
import bricklog.composeapp.generated.resources.feature_set_search_date_filter_btn_one_week
import bricklog.composeapp.generated.resources.feature_set_search_date_filter_btn_one_year
import bricklog.composeapp.generated.resources.feature_set_search_sort_appear_date_descending
import bricklog.composeapp.generated.resources.feature_set_search_sort_launch_date_ascending
import bricklog.composeapp.generated.resources.feature_set_search_sort_launch_date_descending
import bricklog.composeapp.generated.resources.feature_set_search_sort_retiring_date_ascending
import bricklog.composeapp.generated.resources.feature_set_search_sort_retiring_date_descending
import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.StringResource
import kotlin.collections.Set

@Serializable
data class SetFilter(
    val sortOption: SetSortOption? = null,
    val launchDate: DateFilter? = null,
    val appearanceDate: DateFilter? = null,
    val themes: Set<String>? = null,
    val packagingTypes: Set<String>? = null,
    val statuses: Set<SetStatus>? = null,
    val showIncomplete: Boolean? = null,
    val limit: Int? = null,
    val barcode: String? = null,
    val collectionIds: Set<CollectionId>? = null,
)

@Serializable
enum class SetSortOption(
    val titleRes: StringResource,
) {
    LAUNCH_DATE_ASCENDING(
        titleRes = Res.string.feature_set_search_sort_launch_date_ascending
    ),
    LAUNCH_DATE_DESCENDING(
        titleRes = Res.string.feature_set_search_sort_launch_date_descending
    ),
    RETIRING_DATE_ASCENDING(
        titleRes = Res.string.feature_set_search_sort_retiring_date_ascending
    ),
    RETIRING_DATE_DESCENDING(
        titleRes = Res.string.feature_set_search_sort_retiring_date_descending
    ),
    APPEARANCE_DATE_DESCENDING(
        titleRes = Res.string.feature_set_search_sort_appear_date_descending
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
    data class Custom(val startDate: Long? = null, val endDate: Long? = null) :
        DateFilter(DateFilterOption.CUSTOM)
}

enum class DateFilterOption(
    val titleRes: StringResource,
) {
    ANY_TIME(
        titleRes = Res.string.feature_set_search_date_filter_btn_any_time
    ),
    ONE_WEEK(
        titleRes = Res.string.feature_set_search_date_filter_btn_one_week
    ),
    ONE_MONTH(
        titleRes = Res.string.feature_set_search_date_filter_btn_one_month
    ),
    ONE_YEAR(
        titleRes = Res.string.feature_set_search_date_filter_btn_one_year
    ),
    CUSTOM(
        titleRes = Res.string.feature_set_search_date_filter_btn_custom_range
    )
}

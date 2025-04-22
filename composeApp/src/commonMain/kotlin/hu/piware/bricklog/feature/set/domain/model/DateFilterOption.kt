package hu.piware.bricklog.feature.set.domain.model

import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.search_filter_date_any_time
import bricklog.composeapp.generated.resources.search_filter_date_custom_range
import bricklog.composeapp.generated.resources.search_filter_date_one_month
import bricklog.composeapp.generated.resources.search_filter_date_one_week
import bricklog.composeapp.generated.resources.search_filter_date_one_year
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.StringResource

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

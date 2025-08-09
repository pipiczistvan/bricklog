package hu.piware.bricklog.feature.set.presentation.dashboard.utils

import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.feature_set_dashboard_title_arriving_sets
import bricklog.composeapp.generated.resources.feature_set_dashboard_title_latest_releases
import bricklog.composeapp.generated.resources.feature_set_dashboard_title_latest_sets
import bricklog.composeapp.generated.resources.feature_set_dashboard_title_new_items
import bricklog.composeapp.generated.resources.feature_set_dashboard_title_retiring_sets
import hu.piware.bricklog.feature.set.domain.model.DateFilter
import hu.piware.bricklog.feature.set.domain.model.SetFilter
import hu.piware.bricklog.feature.set.domain.model.SetSortOption
import hu.piware.bricklog.feature.set.domain.model.SetStatus
import org.jetbrains.compose.resources.StringResource

enum class FeaturedSetType {
    LATEST_SETS,
    LATEST_RELEASES,
    ARRIVING_SETS,
    RETIRING_SETS,
    NEW_ITEMS,
}

val FeaturedSetType.tag: String
    get() = when (this) {
        FeaturedSetType.LATEST_SETS -> "featured_sets:latest_sets"
        FeaturedSetType.LATEST_RELEASES -> "featured_sets:latest_releases"
        FeaturedSetType.ARRIVING_SETS -> "featured_sets:arriving_sets"
        FeaturedSetType.RETIRING_SETS -> "featured_sets:retiring_sets"
        FeaturedSetType.NEW_ITEMS -> "featured_sets:new_items"
    }

val FeaturedSetType.stringResource: StringResource
    get() = when (this) {
        FeaturedSetType.LATEST_SETS -> Res.string.feature_set_dashboard_title_latest_sets
        FeaturedSetType.LATEST_RELEASES -> Res.string.feature_set_dashboard_title_latest_releases
        FeaturedSetType.ARRIVING_SETS -> Res.string.feature_set_dashboard_title_arriving_sets
        FeaturedSetType.RETIRING_SETS -> Res.string.feature_set_dashboard_title_retiring_sets
        FeaturedSetType.NEW_ITEMS -> Res.string.feature_set_dashboard_title_new_items
    }

val FeaturedSetType.filter: SetFilter
    get() = when (this) {
        FeaturedSetType.LATEST_SETS -> latestSetsFilter
        FeaturedSetType.LATEST_RELEASES -> latestReleasesFilter
        FeaturedSetType.ARRIVING_SETS -> arrivingSetsFilter
        FeaturedSetType.RETIRING_SETS -> retiringSetsFilter
        FeaturedSetType.NEW_ITEMS -> newItemsFilter
    }

val latestSetsFilter = SetFilter(
    sortOption = SetSortOption.APPEARANCE_DATE_DESCENDING,
    launchDate = DateFilter.AnyTime,
    themes = emptyList(),
    packagingTypes = listOf("Box"),
    statuses = emptyList(),
    showIncomplete = false,
    collectionIds = emptyList(),
)

val latestReleasesFilter = SetFilter(
    sortOption = SetSortOption.LAUNCH_DATE_DESCENDING,
    launchDate = DateFilter.AnyTime,
    themes = emptyList(),
    packagingTypes = listOf("Box"),
    statuses = listOf(SetStatus.ACTIVE),
    showIncomplete = false,
    collectionIds = emptyList(),
)

val arrivingSetsFilter = SetFilter(
    sortOption = SetSortOption.LAUNCH_DATE_ASCENDING,
    launchDate = DateFilter.AnyTime,
    themes = emptyList(),
    packagingTypes = listOf("Box"),
    statuses = listOf(SetStatus.ARRIVES_SOON, SetStatus.FUTURE_RELEASE),
    showIncomplete = false,
    collectionIds = emptyList(),
)

val retiringSetsFilter = SetFilter(
    sortOption = SetSortOption.RETIRING_DATE_ASCENDING,
    launchDate = DateFilter.AnyTime,
    themes = emptyList(),
    packagingTypes = listOf("Box"),
    statuses = listOf(SetStatus.RETIRED_SOON),
    showIncomplete = false,
    collectionIds = emptyList(),
)

val newItemsFilter = SetFilter(
    sortOption = SetSortOption.APPEARANCE_DATE_DESCENDING,
    launchDate = DateFilter.AnyTime,
    themes = emptyList(),
    packagingTypes = emptyList(),
    statuses = emptyList(),
    appearanceDate = DateFilter.AnyTime,
    showIncomplete = false,
    collectionIds = emptyList(),
)

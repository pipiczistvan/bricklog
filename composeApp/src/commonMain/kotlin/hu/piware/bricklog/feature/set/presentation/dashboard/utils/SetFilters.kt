package hu.piware.bricklog.feature.set.presentation.dashboard.utils

import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.feature_set_dashboard_title_arriving_sets
import bricklog.composeapp.generated.resources.feature_set_dashboard_title_latest_releases
import bricklog.composeapp.generated.resources.feature_set_dashboard_title_latest_sets
import bricklog.composeapp.generated.resources.feature_set_dashboard_title_new_items
import bricklog.composeapp.generated.resources.feature_set_dashboard_title_retiring_sets
import hu.piware.bricklog.feature.set.domain.model.SetFilter
import hu.piware.bricklog.feature.set.domain.model.SetSortOption
import hu.piware.bricklog.feature.set.domain.model.SetStatus
import hu.piware.bricklog.feature.settings.domain.model.DEFAULT_SET_FILTER_PREFERENCES
import hu.piware.bricklog.feature.settings.domain.model.toSetFilter
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

val latestSetsFilter = DEFAULT_SET_FILTER_PREFERENCES.copy(
    sortOption = SetSortOption.APPEARANCE_DATE_DESCENDING,
).toSetFilter()

val latestReleasesFilter = DEFAULT_SET_FILTER_PREFERENCES.copy(
    sortOption = SetSortOption.LAUNCH_DATE_DESCENDING,
    statuses = listOf(SetStatus.ACTIVE),
).toSetFilter()

val arrivingSetsFilter = DEFAULT_SET_FILTER_PREFERENCES.copy(
    sortOption = SetSortOption.LAUNCH_DATE_ASCENDING,
    statuses = listOf(SetStatus.ARRIVES_SOON, SetStatus.FUTURE_RELEASE),
).toSetFilter()

val retiringSetsFilter = DEFAULT_SET_FILTER_PREFERENCES.copy(
    sortOption = SetSortOption.RETIRING_DATE_ASCENDING,
    statuses = listOf(SetStatus.RETIRED_SOON),
).toSetFilter()

val newItemsFilter = DEFAULT_SET_FILTER_PREFERENCES.copy(
    sortOption = SetSortOption.APPEARANCE_DATE_DESCENDING,
    packagingTypes = emptyList(),
).toSetFilter()

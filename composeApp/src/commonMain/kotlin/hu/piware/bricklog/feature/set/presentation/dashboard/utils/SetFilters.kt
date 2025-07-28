package hu.piware.bricklog.feature.set.presentation.dashboard.utils

import hu.piware.bricklog.feature.set.domain.model.DateFilter
import hu.piware.bricklog.feature.set.domain.model.SetFilter
import hu.piware.bricklog.feature.set.domain.model.SetSortOption
import hu.piware.bricklog.feature.set.domain.model.SetStatus

val latestSetsFilter = SetFilter(
    sortOption = SetSortOption.APPEARANCE_DATE_DESCENDING,
    launchDate = DateFilter.AnyTime,
    themes = emptySet(),
    packagingTypes = setOf("Box"),
    statuses = emptySet(),
    showIncomplete = false,
    collectionIds = emptySet(),
)

val latestReleasesFilter = SetFilter(
    sortOption = SetSortOption.LAUNCH_DATE_DESCENDING,
    launchDate = DateFilter.AnyTime,
    themes = emptySet(),
    packagingTypes = setOf("Box"),
    statuses = setOf(SetStatus.ACTIVE),
    showIncomplete = false,
    collectionIds = emptySet(),
)

val arrivingSetsFilter = SetFilter(
    sortOption = SetSortOption.LAUNCH_DATE_ASCENDING,
    launchDate = DateFilter.AnyTime,
    themes = emptySet(),
    packagingTypes = setOf("Box"),
    statuses = setOf(SetStatus.ARRIVES_SOON, SetStatus.FUTURE_RELEASE),
    showIncomplete = false,
    collectionIds = emptySet(),
)

val retiringSetsFilter = SetFilter(
    sortOption = SetSortOption.RETIRING_DATE_ASCENDING,
    launchDate = DateFilter.AnyTime,
    themes = emptySet(),
    packagingTypes = setOf("Box"),
    statuses = setOf(SetStatus.RETIRED_SOON),
    showIncomplete = false,
    collectionIds = emptySet(),
)

val newItemsFilter = SetFilter(
    sortOption = SetSortOption.APPEARANCE_DATE_DESCENDING,
    launchDate = DateFilter.AnyTime,
    themes = emptySet(),
    packagingTypes = emptySet(),
    statuses = emptySet(),
    appearanceDate = DateFilter.AnyTime,
    showIncomplete = false,
    collectionIds = emptySet(),
)

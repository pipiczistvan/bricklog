package hu.piware.bricklog.feature.set.presentation.dashboard.utils

import hu.piware.bricklog.feature.set.domain.model.DateFilter
import hu.piware.bricklog.feature.set.domain.model.SetFilter
import hu.piware.bricklog.feature.set.domain.model.SetSortOption
import hu.piware.bricklog.feature.set.domain.model.SetStatus

val latestSetsFilter = SetFilter(
    sortOption = SetSortOption.APPEARANCE_DATE_DESCENDING,
    statuses = emptySet(),
    launchDate = DateFilter.AnyTime,
    collectionIds = emptySet(),
    showIncomplete = false
)

val latestReleasesFilter = SetFilter(
    sortOption = SetSortOption.LAUNCH_DATE_DESCENDING,
    statuses = setOf(SetStatus.ACTIVE),
    launchDate = DateFilter.AnyTime,
    collectionIds = emptySet(),
    showIncomplete = false
)

val arrivingSetsFilter = SetFilter(
    sortOption = SetSortOption.LAUNCH_DATE_ASCENDING,
    statuses = setOf(SetStatus.ARRIVES_SOON, SetStatus.FUTURE_RELEASE),
    launchDate = DateFilter.AnyTime,
    collectionIds = emptySet(),
    showIncomplete = false
)

val retiringSetsFilter = SetFilter(
    sortOption = SetSortOption.RETIRING_DATE_ASCENDING,
    statuses = setOf(SetStatus.RETIRED_SOON),
    launchDate = DateFilter.AnyTime,
    collectionIds = emptySet(),
    showIncomplete = false
)

val newSetsNotificationFilter = SetFilter(
    sortOption = SetSortOption.APPEARANCE_DATE_DESCENDING,
    statuses = emptySet(),
    launchDate = DateFilter.AnyTime,
    appearanceDate = DateFilter.AnyTime,
    themes = emptySet(),
    packagingTypes = emptySet(),
    collectionIds = emptySet(),
    showIncomplete = false
)

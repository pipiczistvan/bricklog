package hu.piware.bricklog.feature.set.presentation.dashboard.utils

import hu.piware.bricklog.feature.set.domain.model.DateFilter
import hu.piware.bricklog.feature.set.domain.model.SetFilter
import hu.piware.bricklog.feature.set.domain.model.SetSortOption
import hu.piware.bricklog.feature.set.domain.model.StatusFilterOption

val latestSetsFilter = SetFilter(
    sortOption = SetSortOption.APPEARANCE_DATE_DESCENDING,
    status = StatusFilterOption.ANY_STATUS,
    launchDate = DateFilter.AnyTime,
    showIncomplete = false
)

val arrivingSetsFilter = SetFilter(
    sortOption = SetSortOption.LAUNCH_DATE_ASCENDING,
    status = StatusFilterOption.FUTURE,
    launchDate = DateFilter.AnyTime,
    showIncomplete = false
)

val retiringSetsFilter = SetFilter(
    sortOption = SetSortOption.RETIRING_DATE_ASCENDING,
    status = StatusFilterOption.ACTIVE,
    launchDate = DateFilter.AnyTime,
    showIncomplete = false
)

val newSetsNotificationFilter = SetFilter(
    sortOption = SetSortOption.APPEARANCE_DATE_DESCENDING,
    launchDate = DateFilter.AnyTime,
    appearanceDate = DateFilter.AnyTime,
    themes = emptySet(),
    packagingTypes = emptySet(),
    status = StatusFilterOption.ANY_STATUS,
    showIncomplete = false
)

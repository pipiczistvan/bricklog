package hu.piware.bricklog.feature.set.presentation.dashboard.utils

import hu.piware.bricklog.feature.set.domain.model.SetFilter
import hu.piware.bricklog.feature.set.domain.model.SetSortOption
import hu.piware.bricklog.feature.set.domain.model.StatusFilterOption

val latestSetsFilter = SetFilter(
    sortOption = SetSortOption.LAUNCH_DATE_DESCENDING,
    status = StatusFilterOption.ACTIVE
)

val retiringSetsFilter = SetFilter(
    sortOption = SetSortOption.RETIRING_DATE_ASCENDING,
    status = StatusFilterOption.ACTIVE
)

val favouriteSetsFilter = SetFilter(
    showIncomplete = true,
    isFavourite = true,
    sortOption = SetSortOption.LAUNCH_DATE_DESCENDING
)

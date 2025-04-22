package hu.piware.bricklog.feature.settings.domain.model

import hu.piware.bricklog.feature.set.domain.model.DateFilter
import hu.piware.bricklog.feature.set.domain.model.SetSortOption
import hu.piware.bricklog.feature.set.domain.model.StatusFilterOption
import kotlinx.serialization.Serializable

@Serializable
data class SetFilterPreferences(
    val showOnlyActive: Boolean = true,
    val sortOption: SetSortOption = SetSortOption.LAUNCH_DATE_DESCENDING,
    val launchDate: DateFilter = DateFilter.AnyTime,
    val themes: Set<String> = emptySet(),
    val status: StatusFilterOption = StatusFilterOption.ANY_STATUS,
    val showIncomplete: Boolean = false,
)

package hu.piware.bricklog.feature.set.presentation.set_list

import hu.piware.bricklog.feature.set.domain.model.SetFilter
import hu.piware.bricklog.feature.set.domain.model.SetFilterDomain
import hu.piware.bricklog.feature.set.domain.model.SetListDisplayMode
import hu.piware.bricklog.feature.settings.domain.model.SetFilterPreferences

data class SetListState(
    val title: String = "",
    val filterOverrides: SetFilter? = null,
    val filterPreferences: SetFilterPreferences = SetFilterPreferences(),
    val displayMode: SetListDisplayMode = SetListDisplayMode.COLUMN,
    val showFilterBar: Boolean = true,
    val filterDomain: SetFilterDomain = SetFilterDomain(),
)

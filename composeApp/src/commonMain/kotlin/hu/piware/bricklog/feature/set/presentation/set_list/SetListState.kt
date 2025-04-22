package hu.piware.bricklog.feature.set.presentation.set_list

import hu.piware.bricklog.feature.set.domain.model.SetFilter
import hu.piware.bricklog.feature.set.domain.model.SetListDisplayMode

data class SetListState(
    val title: String = "",
    val filter: SetFilter = SetFilter(),
    val themeMultiSelectEnabled: Boolean = true,
    val displayMode: SetListDisplayMode = SetListDisplayMode.COLUMN
)

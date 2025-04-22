package hu.piware.bricklog.feature.set.presentation.set_list

import hu.piware.bricklog.feature.set.domain.model.SetFilter
import hu.piware.bricklog.feature.set.domain.model.SetListDisplayMode
import hu.piware.bricklog.feature.set.presentation.set_detail.SetDetailArguments
import hu.piware.bricklog.feature.set.presentation.theme_multi_select.ThemeMultiSelectArguments

sealed interface SetListAction {
    data object OnBackClick : SetListAction
    data class OnThemeMultiselectClick(val arguments: ThemeMultiSelectArguments) : SetListAction
    data class OnSetClick(val arguments: SetDetailArguments) : SetListAction
    data class OnFavouriteClick(val setId: Int) : SetListAction
    data class OnFilterChange(val filter: SetFilter) : SetListAction
    data class OnDisplayModeChange(val mode: SetListDisplayMode) : SetListAction
}

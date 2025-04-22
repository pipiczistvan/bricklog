package hu.piware.bricklog.feature.set.presentation.dashboard.components.search_bar

import hu.piware.bricklog.feature.set.domain.model.SetFilter
import hu.piware.bricklog.feature.set.presentation.set_detail.SetDetailArguments
import hu.piware.bricklog.feature.set.presentation.set_list.SetListArguments
import hu.piware.bricklog.feature.set.presentation.theme_multi_select.ThemeMultiSelectArguments

sealed interface SetSearchBarAction {
    data class OnQueryChange(val query: String) : SetSearchBarAction
    data class OnFilterChange(val filter: SetFilter) : SetSearchBarAction
    data object OnClearClick : SetSearchBarAction
    data object OnDrawerClick : SetSearchBarAction
    data object OnScanClick : SetSearchBarAction
    data class OnThemeMultiselectClick(val arguments: ThemeMultiSelectArguments) :
        SetSearchBarAction

    data class OnShowAllClick(val arguments: SetListArguments) : SetSearchBarAction
    data class OnSetClick(val arguments: SetDetailArguments) : SetSearchBarAction
}

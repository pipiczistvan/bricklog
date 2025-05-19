package hu.piware.bricklog.feature.set.presentation.dashboard.components.search_bar

import hu.piware.bricklog.feature.set.presentation.set_detail.SetDetailArguments
import hu.piware.bricklog.feature.set.presentation.set_list.SetListArguments
import hu.piware.bricklog.feature.settings.domain.model.SetFilterPreferences

sealed interface SetSearchBarAction {
    data class OnQueryChange(val query: String) : SetSearchBarAction
    data class OnFilterChange(val filterPreferences: SetFilterPreferences) : SetSearchBarAction
    data object OnClearClick : SetSearchBarAction
    data object OnDrawerClick : SetSearchBarAction
    data object OnScanClick : SetSearchBarAction
    data class OnShowAllClick(val arguments: SetListArguments) : SetSearchBarAction
    data class OnSetClick(val arguments: SetDetailArguments) : SetSearchBarAction
}

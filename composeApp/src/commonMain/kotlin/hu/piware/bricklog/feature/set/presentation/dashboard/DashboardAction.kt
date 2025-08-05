package hu.piware.bricklog.feature.set.presentation.dashboard

import hu.piware.bricklog.feature.set.presentation.set_detail.SetDetailArguments
import hu.piware.bricklog.feature.set.presentation.set_list.SetListArguments

sealed interface DashboardAction {
    data class OnSetClick(val arguments: SetDetailArguments) : DashboardAction
    data class OnSearchSets(val arguments: SetListArguments) : DashboardAction
    data object OnThemeListClick : DashboardAction
    data object OnRefreshData : DashboardAction
    data object OnUpdateChangelogReadVersion : DashboardAction
}

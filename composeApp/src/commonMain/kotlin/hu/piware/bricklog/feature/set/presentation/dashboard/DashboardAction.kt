package hu.piware.bricklog.feature.set.presentation.dashboard

import hu.piware.bricklog.feature.set.presentation.set_detail.SetDetailArguments
import hu.piware.bricklog.feature.set.presentation.set_list.SetListArguments

sealed interface DashboardAction {
    data class OnSetClick(val arguments: SetDetailArguments) : DashboardAction
    data class OnSearchSets(val arguments: SetListArguments) : DashboardAction
    data object OnNotificationSettingsClick : DashboardAction
    data object OnAboutClick : DashboardAction
    data object OnAppearanceClick : DashboardAction
    data object OnRefreshSets : DashboardAction
}

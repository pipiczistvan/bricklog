package hu.piware.bricklog.feature.set.presentation.dashboard

import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.set.presentation.set_detail.SetDetailArguments
import hu.piware.bricklog.feature.set.presentation.set_list.SetListArguments
import kotlinx.datetime.Instant

sealed interface DashboardAction {
    data class OnSetClick(val arguments: SetDetailArguments) : DashboardAction
    data class OnSearchSets(val arguments: SetListArguments) : DashboardAction
    data object OnNotificationSettingsClick : DashboardAction
    data object OnAboutClick : DashboardAction
    data object OnAppearanceClick : DashboardAction
    data class OnCollectionEditClick(val id: CollectionId) : DashboardAction
    data object OnThemeListClick : DashboardAction
    data object OnRefreshSets : DashboardAction
    data class OnResetSets(val date: Instant) : DashboardAction
    data object OnUpdateChangelogReadVersion : DashboardAction
}

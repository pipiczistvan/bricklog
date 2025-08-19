package hu.piware.bricklog.feature.set.presentation.dashboard.components.navigation_drawer

import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.set.presentation.set_list.SetListArguments
import kotlinx.datetime.Instant

sealed interface DashboardNavigationDrawerAction {
    data class OnCollectionEditClick(val id: CollectionId?) : DashboardNavigationDrawerAction
    data class OnSearchSets(val arguments: SetListArguments) : DashboardNavigationDrawerAction
    data class OnResetSets(val date: Instant) : DashboardNavigationDrawerAction
    data object OnLoginClick : DashboardNavigationDrawerAction
    data object OnLogoutClick : DashboardNavigationDrawerAction
    data object OnLogoutConfirm : DashboardNavigationDrawerAction
    data object OnLogoutDismiss : DashboardNavigationDrawerAction
    data object OnDeleteUserClick : DashboardNavigationDrawerAction
    data object OnDeleteUserDismiss : DashboardNavigationDrawerAction
    data object OnDeleteUserConfirm : DashboardNavigationDrawerAction
    data object OnNotificationSettingsClick : DashboardNavigationDrawerAction
    data object OnAboutClick : DashboardNavigationDrawerAction
    data object OnAppearanceClick : DashboardNavigationDrawerAction
    data object OnUserDetailsClick : DashboardNavigationDrawerAction
}

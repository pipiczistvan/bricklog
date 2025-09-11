package hu.piware.bricklog.feature.set.presentation.dashboard.components.navigation_drawer

import kotlinx.datetime.Instant

sealed interface DashboardNavigationDrawerAction {
    data class OnResetSets(val date: Instant) : DashboardNavigationDrawerAction
    data object OnLoginClick : DashboardNavigationDrawerAction
    data object OnLogoutClick : DashboardNavigationDrawerAction
    data object OnLogoutConfirm : DashboardNavigationDrawerAction
    data object OnLogoutDismiss : DashboardNavigationDrawerAction
    data object OnNotificationSettingsClick : DashboardNavigationDrawerAction
    data object OnAboutClick : DashboardNavigationDrawerAction
    data object OnAppearanceClick : DashboardNavigationDrawerAction
    data object OnFriendListClick : DashboardNavigationDrawerAction
}

package hu.piware.bricklog.feature.set.presentation.dashboard

import hu.piware.bricklog.feature.set.domain.model.SetDetails
import hu.piware.bricklog.feature.settings.domain.model.Changelog
import hu.piware.bricklog.feature.settings.domain.model.UserPreferences
import hu.piware.bricklog.feature.user.domain.model.User

data class DashboardState(
    val latestSets: List<SetDetails> = emptyList(),
    val latestReleases: List<SetDetails> = emptyList(),
    val arrivingSets: List<SetDetails> = emptyList(),
    val retiringSets: List<SetDetails> = emptyList(),
    val newItems: List<SetDetails> = emptyList(),
    val currentUser: User? = null,
    val showLogoutConfirm: Boolean = false,
    val showDeleteUserConfirm: Boolean = false,
    val areSetsRefreshing: Boolean = false,
    val changelog: Changelog? = null,
    val userPreferences: UserPreferences = UserPreferences(),
)

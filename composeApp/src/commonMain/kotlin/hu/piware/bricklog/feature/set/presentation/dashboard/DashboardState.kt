package hu.piware.bricklog.feature.set.presentation.dashboard

import hu.piware.bricklog.feature.collection.domain.model.CollectionSetDetails
import hu.piware.bricklog.feature.set.domain.model.SetDetails
import hu.piware.bricklog.feature.settings.domain.model.Changelog
import hu.piware.bricklog.feature.user.domain.manager.SessionManager.Companion.GUEST_PREFERENCES
import hu.piware.bricklog.feature.user.domain.manager.SessionManager.Companion.GUEST_USER
import hu.piware.bricklog.feature.user.domain.model.User
import hu.piware.bricklog.feature.user.domain.model.UserPreferences

data class DashboardState(
    val latestSets: List<SetDetails>? = null,
    val latestReleases: List<SetDetails>? = null,
    val arrivingSets: List<SetDetails>? = null,
    val retiringSets: List<SetDetails>? = null,
    val newItems: List<SetDetails>? = null,
    val currentUser: User = GUEST_USER,
    val showLogoutConfirm: Boolean = false,
    val areSetsRefreshing: Boolean = false,
    val changelog: Changelog? = null,
    val userPreferences: UserPreferences = GUEST_PREFERENCES,
    val collectionSetDetails: List<CollectionSetDetails>? = null,
)

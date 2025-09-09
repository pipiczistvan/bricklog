package hu.piware.bricklog.feature.set.presentation.dashboard.components.search_bar

import hu.piware.bricklog.feature.set.domain.model.SetDetails
import hu.piware.bricklog.feature.set.domain.model.SetFilterDomain
import hu.piware.bricklog.feature.settings.domain.model.SetFilterPreferences
import hu.piware.bricklog.feature.user.domain.manager.SessionManager.Companion.GUEST_USER
import hu.piware.bricklog.feature.user.domain.model.User

data class SetSearchBarState(
    val typedQuery: String = "",
    val searchQuery: String = "",
    val filterPreferences: SetFilterPreferences = SetFilterPreferences(),
    val searchResults: List<SetDetails> = emptyList(),
    val filterDomain: SetFilterDomain = SetFilterDomain(),
    val user: User = GUEST_USER,
)

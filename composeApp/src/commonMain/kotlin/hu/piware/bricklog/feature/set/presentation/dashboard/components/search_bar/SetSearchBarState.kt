package hu.piware.bricklog.feature.set.presentation.dashboard.components.search_bar

import hu.piware.bricklog.feature.set.domain.model.SetUI
import hu.piware.bricklog.feature.settings.domain.model.SetFilterPreferences

data class SetSearchBarState(
    val typedQuery: String = "",
    val searchQuery: String = "",
    val filterPreferences: SetFilterPreferences = SetFilterPreferences(),
    val searchResults: List<SetUI> = emptyList(),
)

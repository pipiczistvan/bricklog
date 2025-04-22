package hu.piware.bricklog.feature.set.presentation.dashboard.components.search_bar

import hu.piware.bricklog.feature.set.domain.model.SetFilter
import hu.piware.bricklog.feature.set.domain.model.SetUI

data class SetSearchBarState(
    val typedQuery: String = "",
    val searchQuery: String = "",
    val filter: SetFilter = SetFilter(),
    val searchResults: List<SetUI> = emptyList(),
)

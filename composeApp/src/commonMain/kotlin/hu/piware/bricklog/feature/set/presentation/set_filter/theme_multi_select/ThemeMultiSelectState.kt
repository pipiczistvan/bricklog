package hu.piware.bricklog.feature.set.presentation.set_filter.theme_multi_select

data class ThemeMultiSelectState(
    val availableThemes: List<String> = emptyList(),
    val selectedThemes: Set<String> = emptySet(),
    val searchQuery: String = "",
)

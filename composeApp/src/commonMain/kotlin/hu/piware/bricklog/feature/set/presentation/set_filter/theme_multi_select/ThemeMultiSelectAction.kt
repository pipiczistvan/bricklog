package hu.piware.bricklog.feature.set.presentation.set_filter.theme_multi_select

sealed interface ThemeMultiSelectAction {
    data class OnQueryChange(val query: String) : ThemeMultiSelectAction
    data object OnBackClick : ThemeMultiSelectAction
    data class OnApplyClick(val arguments: ThemeMultiSelectArguments) : ThemeMultiSelectAction
    data class OnSelectionChange(val themes: Set<String>) : ThemeMultiSelectAction
}

package hu.piware.bricklog.feature.set.presentation.theme_multi_select

sealed interface ThemeMultiSelectAction {
    data class OnQueryChange(val query: String) : ThemeMultiSelectAction
    data object OnBackClick : ThemeMultiSelectAction
    data class OnApplyClick(val arguments: ThemeMultiSelectArguments) : ThemeMultiSelectAction
    data class OnThemeSelectionChange(val themes: Set<String>) : ThemeMultiSelectAction
}

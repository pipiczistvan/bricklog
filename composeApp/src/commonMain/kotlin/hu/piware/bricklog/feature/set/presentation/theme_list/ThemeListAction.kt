package hu.piware.bricklog.feature.set.presentation.theme_list

import hu.piware.bricklog.feature.set.presentation.set_list.SetListArguments

sealed interface ThemeListAction {
    data object OnBackClick : ThemeListAction
    data class OnSearchSets(val arguments: SetListArguments) : ThemeListAction
}

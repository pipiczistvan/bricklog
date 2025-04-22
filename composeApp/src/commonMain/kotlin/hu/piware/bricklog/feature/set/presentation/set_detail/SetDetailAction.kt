package hu.piware.bricklog.feature.set.presentation.set_detail

sealed interface SetDetailAction {
    data object OnBackClick : SetDetailAction
    data class OnFavouriteClick(val setId: Int) : SetDetailAction
    data class OnImageClick(val setId: Int) : SetDetailAction
}

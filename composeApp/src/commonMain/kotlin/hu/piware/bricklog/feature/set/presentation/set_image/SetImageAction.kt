package hu.piware.bricklog.feature.set.presentation.set_image

sealed interface SetImageAction {
    data object OnBackClick : SetImageAction
}

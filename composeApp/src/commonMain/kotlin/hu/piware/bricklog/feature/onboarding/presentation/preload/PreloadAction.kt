package hu.piware.bricklog.feature.onboarding.presentation.preload

sealed interface PreloadAction {
    data object OnRetryClick : PreloadAction
    data object OnContinueClick : PreloadAction
}

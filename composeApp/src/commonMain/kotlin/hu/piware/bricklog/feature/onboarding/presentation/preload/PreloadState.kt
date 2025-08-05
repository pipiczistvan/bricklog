package hu.piware.bricklog.feature.onboarding.presentation.preload

import hu.piware.bricklog.feature.core.presentation.UiTextUpdateProgress

sealed interface PreloadState {
    data object Initial : PreloadState
    data class Loading(val progress: UiTextUpdateProgress) : PreloadState
    data object Success : PreloadState
    data object Error : PreloadState
}

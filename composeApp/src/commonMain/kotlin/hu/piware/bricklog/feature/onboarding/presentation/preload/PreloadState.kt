package hu.piware.bricklog.feature.onboarding.presentation.preload

import hu.piware.bricklog.feature.core.domain.usecase.UpdateDataProgress

sealed interface PreloadState {
    data object Initial : PreloadState
    data class Loading(val progress: UpdateDataProgress) : PreloadState
    data object Success : PreloadState
    data object Error : PreloadState
}

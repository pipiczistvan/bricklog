package hu.piware.bricklog.feature.settings.domain.util

import androidx.compose.ui.state.ToggleableState
import hu.piware.bricklog.feature.settings.domain.model.NotificationPreferences

fun NotificationPreferences.generalState(): ToggleableState {
    return when {
        newSets -> ToggleableState.On
        (!newSets) -> ToggleableState.Off
        else -> ToggleableState.Indeterminate
    }
}

package hu.piware.bricklog.feature.settings.presentation.appearance

import hu.piware.bricklog.feature.settings.domain.model.ThemeOption

data class AppearanceState(
    val themeOption: ThemeOption = ThemeOption.SYSTEM,
)

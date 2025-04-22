package hu.piware.bricklog.feature.settings.domain.usecase

import hu.piware.bricklog.feature.settings.domain.repository.SettingsRepository

class WatchThemeOption(
    private val settingsRepository: SettingsRepository,
) {
    operator fun invoke() = settingsRepository.themeOption
}

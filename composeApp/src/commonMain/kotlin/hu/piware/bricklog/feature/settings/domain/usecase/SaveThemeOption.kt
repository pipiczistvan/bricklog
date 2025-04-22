package hu.piware.bricklog.feature.settings.domain.usecase

import hu.piware.bricklog.feature.settings.domain.model.ThemeOption
import hu.piware.bricklog.feature.settings.domain.repository.SettingsRepository

class SaveThemeOption(
    private val settingsRepository: SettingsRepository,
) {
    suspend operator fun invoke(option: ThemeOption) {
        settingsRepository.saveThemeOption(option)
    }
}

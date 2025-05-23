package hu.piware.bricklog.feature.settings.domain.usecase

import hu.piware.bricklog.feature.settings.domain.model.ThemeOption
import hu.piware.bricklog.feature.settings.domain.repository.SettingsRepository
import org.koin.core.annotation.Single

@Single
class SaveThemeOption(
    private val settingsRepository: SettingsRepository,
) {
    suspend operator fun invoke(option: ThemeOption) {
        settingsRepository.saveThemeOption(option)
    }
}

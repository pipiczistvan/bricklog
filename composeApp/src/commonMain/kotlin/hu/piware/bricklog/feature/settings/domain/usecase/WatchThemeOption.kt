package hu.piware.bricklog.feature.settings.domain.usecase

import hu.piware.bricklog.feature.settings.domain.repository.SettingsRepository
import org.koin.core.annotation.Single

@Single
class WatchThemeOption(
    private val settingsRepository: SettingsRepository,
) {
    operator fun invoke() = settingsRepository.watchThemeOption()
}

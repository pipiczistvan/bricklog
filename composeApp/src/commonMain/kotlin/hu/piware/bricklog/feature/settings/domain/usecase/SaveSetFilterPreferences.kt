package hu.piware.bricklog.feature.settings.domain.usecase

import hu.piware.bricklog.feature.settings.domain.model.SetFilterPreferences
import hu.piware.bricklog.feature.settings.domain.repository.SettingsRepository
import org.koin.core.annotation.Single

@Single
class SaveSetFilterPreferences(
    private val settingsRepository: SettingsRepository,
) {
    suspend operator fun invoke(filterPreferences: SetFilterPreferences) {
        settingsRepository.saveSetFilterPreferences(filterPreferences)
    }
}

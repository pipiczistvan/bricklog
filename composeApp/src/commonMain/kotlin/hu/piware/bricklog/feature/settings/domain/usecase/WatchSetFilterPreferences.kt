package hu.piware.bricklog.feature.settings.domain.usecase

import hu.piware.bricklog.feature.settings.domain.model.SetFilterPreferences
import hu.piware.bricklog.feature.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow

class WatchSetFilterPreferences(
    private val settingsRepository: SettingsRepository,
) {
    operator fun invoke(): Flow<SetFilterPreferences> {
        return settingsRepository.setFilterPreferences
    }
}

package hu.piware.bricklog.feature.set.domain.usecase

import hu.piware.bricklog.feature.set.domain.model.SetFilter
import hu.piware.bricklog.feature.settings.data.datastore.toPreferences
import hu.piware.bricklog.feature.settings.domain.repository.SettingsRepository

class SaveSetFilter(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(filter: SetFilter) {
        settingsRepository.saveSetFilterPreferences(filter.toPreferences())
    }
}

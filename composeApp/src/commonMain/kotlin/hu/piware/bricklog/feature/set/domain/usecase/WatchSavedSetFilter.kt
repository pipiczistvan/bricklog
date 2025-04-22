package hu.piware.bricklog.feature.set.domain.usecase

import hu.piware.bricklog.feature.set.domain.model.SetFilter
import hu.piware.bricklog.feature.settings.data.datastore.toDomainModel
import hu.piware.bricklog.feature.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WatchSavedSetFilter(
    private val settingsRepository: SettingsRepository,
) {
    operator fun invoke(): Flow<SetFilter> {
        return settingsRepository.setFilterPreferences.map { it.toDomainModel() }
    }
}

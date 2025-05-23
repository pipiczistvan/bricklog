package hu.piware.bricklog.feature.settings.domain.usecase

import hu.piware.bricklog.feature.set.domain.model.SetListDisplayMode
import hu.piware.bricklog.feature.settings.domain.repository.SettingsRepository
import org.koin.core.annotation.Single

@Single
class SaveSetListDisplayMode(
    private val settingsRepository: SettingsRepository,
) {
    suspend operator fun invoke(mode: SetListDisplayMode) {
        settingsRepository.saveSetListDisplayMode(mode)
    }
}

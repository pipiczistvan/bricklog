package hu.piware.bricklog.feature.set.domain.usecase

import hu.piware.bricklog.feature.settings.domain.repository.SettingsRepository
import hu.piware.bricklog.util.BuildConfig

class UpdateChangelogReadVersion(
    private val settingsRepository: SettingsRepository,
) {
    suspend operator fun invoke() {
        settingsRepository.saveChangelogReadVersion(BuildConfig.VERSION_CODE.toInt())
    }
}

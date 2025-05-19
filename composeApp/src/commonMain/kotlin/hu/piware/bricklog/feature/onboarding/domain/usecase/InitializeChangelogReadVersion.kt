package hu.piware.bricklog.feature.onboarding.domain.usecase

import hu.piware.bricklog.feature.settings.domain.repository.SettingsRepository
import hu.piware.bricklog.util.BuildConfig
import kotlinx.coroutines.flow.first

class InitializeChangelogReadVersion(
    private val settingsRepository: SettingsRepository,
) {
    suspend operator fun invoke() {
        if (settingsRepository.changelogReadVersion.first() == -1) {
            settingsRepository.saveChangelogReadVersion(BuildConfig.VERSION_CODE.toInt())
        }
    }
}

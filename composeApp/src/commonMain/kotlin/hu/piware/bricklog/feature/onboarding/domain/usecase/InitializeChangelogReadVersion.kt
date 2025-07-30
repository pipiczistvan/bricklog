package hu.piware.bricklog.feature.onboarding.domain.usecase

import hu.piware.bricklog.BuildKonfig
import hu.piware.bricklog.feature.settings.domain.repository.SettingsRepository
import hu.piware.bricklog.util.firstOrDefault
import org.koin.core.annotation.Single

@Single
class InitializeChangelogReadVersion(
    private val settingsRepository: SettingsRepository,
) {
    suspend operator fun invoke() {
        if (settingsRepository.watchChangelogReadVersion().firstOrDefault { -1 } == -1) {
            settingsRepository.saveChangelogReadVersion(BuildKonfig.RELEASE_VERSION)
        }
    }
}

package hu.piware.bricklog.feature.set.domain.usecase

import hu.piware.bricklog.feature.settings.domain.repository.SettingsRepository
import hu.piware.bricklog.util.BuildConfig
import org.koin.core.annotation.Single

@Single
class UpdateChangelogReadVersion(
    private val settingsRepository: SettingsRepository,
) {
    suspend operator fun invoke() {
        settingsRepository.saveChangelogReadVersion(BuildConfig.VERSION_CODE.toInt())
    }
}

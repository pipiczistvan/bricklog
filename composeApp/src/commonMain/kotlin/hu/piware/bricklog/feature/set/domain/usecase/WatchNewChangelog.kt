package hu.piware.bricklog.feature.set.domain.usecase

import hu.piware.bricklog.feature.core.domain.onSuccess
import hu.piware.bricklog.feature.settings.domain.model.Changelog
import hu.piware.bricklog.feature.settings.domain.repository.ChangelogRepository
import hu.piware.bricklog.feature.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Single

@Single
class WatchNewChangelog(
    private val settingsRepository: SettingsRepository,
    private val changelogRepository: ChangelogRepository,
) {
    operator fun invoke(): Flow<Changelog> {
        val changelogFlow = flow {
            changelogRepository.getChangelog()
                .onSuccess {
                    emit(it)
                }
        }
        val readVersionFlow = settingsRepository.watchChangelogReadVersion()

        return combine(changelogFlow, readVersionFlow) { changelog, readVersion ->
            changelog.copy(
                releases = changelog.releases.filter { it.build > readVersion },
            )
        }
    }
}

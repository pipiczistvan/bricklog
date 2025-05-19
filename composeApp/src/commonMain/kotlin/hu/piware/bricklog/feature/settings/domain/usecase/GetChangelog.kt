package hu.piware.bricklog.feature.settings.domain.usecase

import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.settings.domain.model.Changelog
import hu.piware.bricklog.feature.settings.domain.repository.ChangelogRepository

class GetChangelog(
    private val changelogRepository: ChangelogRepository,
) {
    suspend operator fun invoke(): Result<Changelog, DataError> {
        return changelogRepository.getChangelog()
    }
}

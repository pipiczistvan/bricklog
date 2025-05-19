package hu.piware.bricklog.feature.settings.domain.repository

import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.settings.domain.model.Changelog

interface ChangelogRepository {
    suspend fun getChangelog(): Result<Changelog, DataError>
}

package hu.piware.bricklog.feature.settings.domain.datasource

import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.settings.domain.model.Changelog

interface LocalChangelogDataSource {
    suspend fun getChangelog(): Result<Changelog, DataError.Local>
}

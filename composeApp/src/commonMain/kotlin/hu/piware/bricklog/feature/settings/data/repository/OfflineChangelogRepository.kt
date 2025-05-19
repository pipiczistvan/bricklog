package hu.piware.bricklog.feature.settings.data.repository

import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.settings.domain.datasource.LocalChangelogDataSource
import hu.piware.bricklog.feature.settings.domain.model.Changelog
import hu.piware.bricklog.feature.settings.domain.repository.ChangelogRepository

class OfflineChangelogRepository(
    private val localDataSource: LocalChangelogDataSource,
) : ChangelogRepository {

    override suspend fun getChangelog(): Result<Changelog, DataError> {
        return localDataSource.getChangelog()
    }
}

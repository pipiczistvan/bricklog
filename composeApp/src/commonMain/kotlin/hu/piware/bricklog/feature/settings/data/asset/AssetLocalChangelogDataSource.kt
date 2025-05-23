@file:OptIn(ExperimentalResourceApi::class)

package hu.piware.bricklog.feature.settings.data.asset

import bricklog.composeapp.generated.resources.Res
import co.touchlab.kermit.Logger
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.settings.domain.datasource.LocalChangelogDataSource
import hu.piware.bricklog.feature.settings.domain.model.Changelog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.koin.core.annotation.Single

@Single
class AssetLocalChangelogDataSource : LocalChangelogDataSource {

    private val logger = Logger.withTag("AssetLocalChangelogDataSource")

    private val json = Json {
        ignoreUnknownKeys = true
    }

    override suspend fun getChangelog(): Result<Changelog, DataError.Local> {
        return withContext(Dispatchers.IO) {
            try {
                val changelogJson = Res.readBytes("files/changelog.json").decodeToString()
                val changelog = json.decodeFromString<Changelog>(changelogJson)

                Result.Success(changelog)
            } catch (e: Exception) {
                logger.e("Failed to read changelog", e)
                Result.Error(DataError.Local.UNKNOWN)
            }
        }
    }
}

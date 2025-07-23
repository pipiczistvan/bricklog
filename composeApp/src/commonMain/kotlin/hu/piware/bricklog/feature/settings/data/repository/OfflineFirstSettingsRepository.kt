@file:OptIn(ExperimentalCoroutinesApi::class)

package hu.piware.bricklog.feature.settings.data.repository

import hu.piware.bricklog.feature.set.domain.model.SetListDisplayMode
import hu.piware.bricklog.feature.settings.domain.datasource.LocalSettingsDataSource
import hu.piware.bricklog.feature.settings.domain.model.LanguageOption
import hu.piware.bricklog.feature.settings.domain.model.NotificationPreferences
import hu.piware.bricklog.feature.settings.domain.model.SetFilterPreferences
import hu.piware.bricklog.feature.settings.domain.model.ThemeOption
import hu.piware.bricklog.feature.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class OfflineFirstSettingsRepository(
    private val localDataSource: LocalSettingsDataSource,
) : SettingsRepository {

    override fun watchSetFilterPreferences(): Flow<SetFilterPreferences> {
        return localDataSource.watchSetFilterPreferences()
    }

    override fun watchNotificationPreferences(): Flow<NotificationPreferences> {
        return localDataSource.watchNotificationPreferences()
    }

    override fun watchLanguageOption(): Flow<LanguageOption> {
        return localDataSource.watchLanguageOption()
    }

    override fun watchThemeOption(): Flow<ThemeOption> {
        return localDataSource.watchThemeOption()
    }

    override fun watchSetListDisplayMode(): Flow<SetListDisplayMode> {
        return localDataSource.watchSetListDisplayMode()
    }

    override fun watchChangelogReadVersion(): Flow<Int> {
        return localDataSource.watchChangelogReadVersion()
    }

    override suspend fun saveSetFilterPreferences(preferences: SetFilterPreferences) {
        localDataSource.saveSetFilterPreferences(preferences)
    }

    override suspend fun saveNotificationPreferences(preferences: NotificationPreferences) {
        localDataSource.saveNotificationPreferences(preferences)
    }

    override suspend fun saveLanguageOption(option: LanguageOption) {
        localDataSource.saveLanguageOption(option)
    }

    override suspend fun saveThemeOption(option: ThemeOption) {
        localDataSource.saveThemeOption(option)
    }

    override suspend fun saveSetListDisplayMode(mode: SetListDisplayMode) {
        localDataSource.saveSetListDisplayMode(mode)
    }

    override suspend fun saveChangelogReadVersion(version: Int) {
        localDataSource.saveChangelogReadVersion(version)
    }
}

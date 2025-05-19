package hu.piware.bricklog.feature.settings.data.repository

import hu.piware.bricklog.feature.set.domain.model.SetListDisplayMode
import hu.piware.bricklog.feature.settings.domain.datasource.LocalSettingsDataSource
import hu.piware.bricklog.feature.settings.domain.model.LanguageOption
import hu.piware.bricklog.feature.settings.domain.model.NotificationPreferences
import hu.piware.bricklog.feature.settings.domain.model.SetFilterPreferences
import hu.piware.bricklog.feature.settings.domain.model.ThemeOption
import hu.piware.bricklog.feature.settings.domain.repository.SettingsRepository

class OfflineSettingsRepository(
    private val localDataSource: LocalSettingsDataSource,
) : SettingsRepository {

    override val setFilterPreferences = localDataSource.watchSetFilterPreferences()

    override val notificationPreferences = localDataSource.watchNotificationPreferences()

    override val languageOption = localDataSource.watchLanguageOption()

    override val themeOption = localDataSource.watchThemeOption()

    override val setListDisplayMode = localDataSource.watchSetListDisplayMode()

    override val changelogReadVersion = localDataSource.watchChangelogReadVersion()

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

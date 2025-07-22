package hu.piware.bricklog.feature.settings.domain.repository

import hu.piware.bricklog.feature.set.domain.model.SetListDisplayMode
import hu.piware.bricklog.feature.settings.domain.model.LanguageOption
import hu.piware.bricklog.feature.settings.domain.model.NotificationPreferences
import hu.piware.bricklog.feature.settings.domain.model.SetFilterPreferences
import hu.piware.bricklog.feature.settings.domain.model.ThemeOption
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val setFilterPreferences: Flow<SetFilterPreferences>
    val notificationPreferences: Flow<NotificationPreferences>
    val languageOption: Flow<LanguageOption>
    val themeOption: Flow<ThemeOption>
    val setListDisplayMode: Flow<SetListDisplayMode>
    val changelogReadVersion: Flow<Int>

    suspend fun saveSetFilterPreferences(preferences: SetFilterPreferences)

    suspend fun saveNotificationPreferences(preferences: NotificationPreferences)

    suspend fun saveLanguageOption(option: LanguageOption)

    suspend fun saveThemeOption(option: ThemeOption)

    suspend fun saveSetListDisplayMode(mode: SetListDisplayMode)

    suspend fun saveChangelogReadVersion(version: Int)
}

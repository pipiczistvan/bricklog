package hu.piware.bricklog.feature.settings.domain.repository

import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.set.domain.model.SetListDisplayMode
import hu.piware.bricklog.feature.settings.domain.model.LanguageOption
import hu.piware.bricklog.feature.settings.domain.model.NotificationPreferences
import hu.piware.bricklog.feature.settings.domain.model.SetFilterPreferences
import hu.piware.bricklog.feature.settings.domain.model.ThemeOption
import hu.piware.bricklog.feature.settings.domain.model.UserPreferences
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val setFilterPreferences: Flow<SetFilterPreferences>
    val notificationPreferences: Flow<NotificationPreferences>
    val languageOption: Flow<LanguageOption>
    val themeOption: Flow<ThemeOption>
    val setListDisplayMode: Flow<SetListDisplayMode>
    val changelogReadVersion: Flow<Int>
    val userPreferences: Flow<UserPreferences>

    suspend fun saveSetFilterPreferences(preferences: SetFilterPreferences)

    suspend fun saveNotificationPreferences(preferences: NotificationPreferences)

    suspend fun saveLanguageOption(option: LanguageOption)

    suspend fun saveThemeOption(option: ThemeOption)

    suspend fun saveSetListDisplayMode(mode: SetListDisplayMode)

    suspend fun saveChangelogReadVersion(version: Int)

    suspend fun saveUserPreferences(preferences: UserPreferences): EmptyResult<DataError>
}

package hu.piware.bricklog.feature.settings.domain.repository

import hu.piware.bricklog.feature.set.domain.model.SetListDisplayMode
import hu.piware.bricklog.feature.settings.domain.model.LanguageOption
import hu.piware.bricklog.feature.settings.domain.model.NotificationPreferences
import hu.piware.bricklog.feature.settings.domain.model.SetFilterPreferences
import hu.piware.bricklog.feature.settings.domain.model.ThemeOption
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    fun watchSetFilterPreferences(): Flow<SetFilterPreferences>

    fun watchNotificationPreferences(): Flow<NotificationPreferences>

    fun watchLanguageOption(): Flow<LanguageOption>

    fun watchThemeOption(): Flow<ThemeOption>

    fun watchSetListDisplayMode(): Flow<SetListDisplayMode>

    fun watchChangelogReadVersion(): Flow<Int>

    suspend fun saveSetFilterPreferences(preferences: SetFilterPreferences)

    suspend fun saveNotificationPreferences(preferences: NotificationPreferences)

    suspend fun saveLanguageOption(option: LanguageOption)

    suspend fun saveThemeOption(option: ThemeOption)

    suspend fun saveSetListDisplayMode(mode: SetListDisplayMode)

    suspend fun saveChangelogReadVersion(version: Int)
}

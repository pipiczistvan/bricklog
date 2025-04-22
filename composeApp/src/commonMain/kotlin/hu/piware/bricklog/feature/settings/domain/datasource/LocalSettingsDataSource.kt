package hu.piware.bricklog.feature.settings.domain.datasource

import hu.piware.bricklog.feature.set.domain.model.SetListDisplayMode
import hu.piware.bricklog.feature.settings.domain.model.LanguageOption
import hu.piware.bricklog.feature.settings.domain.model.NotificationPreferences
import hu.piware.bricklog.feature.settings.domain.model.SetFilterPreferences
import hu.piware.bricklog.feature.settings.domain.model.ThemeOption
import kotlinx.coroutines.flow.Flow

interface LocalSettingsDataSource {

    fun watchSetFilterPreferences(): Flow<SetFilterPreferences>

    fun watchNotificationPreferences(): Flow<NotificationPreferences>

    fun watchLanguageOption(): Flow<LanguageOption>

    fun watchThemeOption(): Flow<ThemeOption>

    fun watchSetListDisplayMode(): Flow<SetListDisplayMode>

    suspend fun saveSetFilterPreferences(filter: SetFilterPreferences)

    suspend fun saveNotificationPreferences(notification: NotificationPreferences)

    suspend fun saveLanguageOption(language: LanguageOption)

    suspend fun saveThemeOption(theme: ThemeOption)

    suspend fun saveSetListDisplayMode(mode: SetListDisplayMode)
}

package hu.piware.bricklog.feature.settings.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import hu.piware.bricklog.feature.set.domain.model.SetListDisplayMode
import hu.piware.bricklog.feature.settings.data.datastore.DataStoreLocalSettingsDataSource.PreferenceKeys.KEY_CHANGELOG_READ_VERSION
import hu.piware.bricklog.feature.settings.data.datastore.DataStoreLocalSettingsDataSource.PreferenceKeys.KEY_LANGUAGE
import hu.piware.bricklog.feature.settings.data.datastore.DataStoreLocalSettingsDataSource.PreferenceKeys.KEY_NOTIFICATION
import hu.piware.bricklog.feature.settings.data.datastore.DataStoreLocalSettingsDataSource.PreferenceKeys.KEY_SET_FILTER
import hu.piware.bricklog.feature.settings.data.datastore.DataStoreLocalSettingsDataSource.PreferenceKeys.KEY_SET_LIST_DISPLAY_MODE
import hu.piware.bricklog.feature.settings.data.datastore.DataStoreLocalSettingsDataSource.PreferenceKeys.KEY_THEME
import hu.piware.bricklog.feature.settings.domain.datasource.LocalSettingsDataSource
import hu.piware.bricklog.feature.settings.domain.model.LanguageOption
import hu.piware.bricklog.feature.settings.domain.model.NotificationPreferences
import hu.piware.bricklog.feature.settings.domain.model.SetFilterPreferences
import hu.piware.bricklog.feature.settings.domain.model.ThemeOption
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Single

@Single
class DataStoreLocalSettingsDataSource(
    private val dataStore: DataStore<Preferences>,
) : LocalSettingsDataSource {

    private val json = Json {
        ignoreUnknownKeys = true
    }

    override fun watchSetFilterPreferences(): Flow<SetFilterPreferences> {
        return dataStore.data
            .map { preferences ->
                preferences[KEY_SET_FILTER]
            }
            .map { jsonString ->
                jsonString?.let { json.decodeFromString(it) } ?: SetFilterPreferences()
            }
    }

    override fun watchNotificationPreferences(): Flow<NotificationPreferences> {
        return dataStore.data
            .map { preferences ->
                preferences[KEY_NOTIFICATION]
            }
            .map { jsonString ->
                jsonString?.let { json.decodeFromString(it) } ?: NotificationPreferences()
            }
    }

    override fun watchLanguageOption(): Flow<LanguageOption> {
        return dataStore.data
            .map { preferences ->
                preferences[KEY_LANGUAGE]
            }
            .map { jsonString ->
                jsonString?.let { json.decodeFromString(it) } ?: LanguageOption.SYSTEM
            }
    }

    override fun watchThemeOption(): Flow<ThemeOption> {
        return dataStore.data
            .map { preferences ->
                preferences[KEY_THEME]
            }
            .map { jsonString ->
                jsonString?.let { json.decodeFromString(it) } ?: ThemeOption.SYSTEM
            }
    }

    override fun watchSetListDisplayMode(): Flow<SetListDisplayMode> {
        return dataStore.data
            .map { preferences ->
                preferences[KEY_SET_LIST_DISPLAY_MODE]
            }
            .map { jsonString ->
                jsonString?.let { json.decodeFromString(it) } ?: SetListDisplayMode.COLUMN
            }
    }

    override fun watchChangelogReadVersion(): Flow<Int> {
        return dataStore.data
            .map { preferences ->
                preferences[KEY_CHANGELOG_READ_VERSION]
            }
            .map { version ->
                version ?: -1
            }
    }

    override suspend fun saveSetFilterPreferences(filter: SetFilterPreferences) {
        dataStore.edit { preferences ->
            preferences[KEY_SET_FILTER] = json.encodeToString(filter)
        }
    }

    override suspend fun saveNotificationPreferences(notification: NotificationPreferences) {
        dataStore.edit { preferences ->
            preferences[KEY_NOTIFICATION] = json.encodeToString(notification)
        }
    }

    override suspend fun saveLanguageOption(language: LanguageOption) {
        dataStore.edit { preferences ->
            preferences[KEY_LANGUAGE] = json.encodeToString(language)
        }
    }

    override suspend fun saveThemeOption(theme: ThemeOption) {
        dataStore.edit { preferences ->
            preferences[KEY_THEME] = json.encodeToString(theme)
        }
    }

    override suspend fun saveSetListDisplayMode(mode: SetListDisplayMode) {
        dataStore.edit { preferences ->
            preferences[KEY_SET_LIST_DISPLAY_MODE] = json.encodeToString(mode)
        }
    }

    override suspend fun saveChangelogReadVersion(version: Int) {
        dataStore.edit { preferences ->
            preferences[KEY_CHANGELOG_READ_VERSION] = version
        }
    }

    private object PreferenceKeys {
        val KEY_SET_FILTER = stringPreferencesKey(name = "set_filter")
        val KEY_NOTIFICATION = stringPreferencesKey(name = "notification")
        val KEY_LANGUAGE = stringPreferencesKey(name = "language")
        val KEY_THEME = stringPreferencesKey(name = "theme")
        val KEY_SET_LIST_DISPLAY_MODE = stringPreferencesKey(name = "set_list_display_mode")
        val KEY_CHANGELOG_READ_VERSION = intPreferencesKey(name = "changelog_read_version")
    }
}

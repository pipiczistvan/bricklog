@file:OptIn(ExperimentalCoroutinesApi::class)

package hu.piware.bricklog.feature.settings.data.repository

import co.touchlab.kermit.Logger
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.core.domain.SyncedRepository
import hu.piware.bricklog.feature.core.domain.onError
import hu.piware.bricklog.feature.set.domain.model.SetListDisplayMode
import hu.piware.bricklog.feature.settings.domain.datasource.LocalSettingsDataSource
import hu.piware.bricklog.feature.settings.domain.datasource.RemoteSettingsDataSource
import hu.piware.bricklog.feature.settings.domain.model.LanguageOption
import hu.piware.bricklog.feature.settings.domain.model.NotificationPreferences
import hu.piware.bricklog.feature.settings.domain.model.SetFilterPreferences
import hu.piware.bricklog.feature.settings.domain.model.ThemeOption
import hu.piware.bricklog.feature.settings.domain.model.UserPreferences
import hu.piware.bricklog.feature.settings.domain.repository.SettingsRepository
import hu.piware.bricklog.feature.user.domain.manager.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.core.annotation.Single

@Single
class OfflineFirstSettingsRepository(
    private val localDataSource: LocalSettingsDataSource,
    private val remoteDataSource: RemoteSettingsDataSource,
    private val sessionManager: SessionManager,
) : SettingsRepository, SyncedRepository {

    private val logger = Logger.withTag("OfflineFirstSettingsRepository")

    override fun startSync(scope: CoroutineScope) {
        sessionManager.currentUser
            .filterNotNull()
            .flatMapLatest { user ->
                remoteDataSource.watchUserPreferences(user.uid)
            }
            .onEach { remotePreferences ->
                logger.d { "Syncing user preferences" }
                if (remotePreferences == null) {
                    clearLocal()
                } else {
                    localDataSource.saveUserPreferences(remotePreferences)
                }
            }
            .launchIn(scope)
    }

    override suspend fun clearLocal(): EmptyResult<DataError> {
        localDataSource.removeUserPreferences()
        return Result.Success(Unit)
    }

    override val setFilterPreferences = localDataSource.watchSetFilterPreferences()

    override val notificationPreferences = localDataSource.watchNotificationPreferences()

    override val languageOption = localDataSource.watchLanguageOption()

    override val themeOption = localDataSource.watchThemeOption()

    override val setListDisplayMode = localDataSource.watchSetListDisplayMode()

    override val changelogReadVersion = localDataSource.watchChangelogReadVersion()

    override val userPreferences = localDataSource.watchUserPreferences()

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

    override suspend fun saveUserPreferences(preferences: UserPreferences): EmptyResult<DataError> {
        localDataSource.saveUserPreferences(preferences)

        val user = sessionManager.currentUser.value
        if (user != null) {
            remoteDataSource.saveUserPreferences(user.uid, preferences)
                .onError { return it }
        }

        return Result.Success(Unit)
    }
}

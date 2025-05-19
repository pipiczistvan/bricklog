package hu.piware.bricklog.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import hu.piware.bricklog.feature.core.data.database.BricklogDatabase
import hu.piware.bricklog.feature.core.data.database.DatabaseFactory
import hu.piware.bricklog.feature.core.data.datastore.DatastoreFactory
import hu.piware.bricklog.feature.core.data.network.HttpClientFactory
import hu.piware.bricklog.feature.onboarding.domain.usecase.HasAnySets
import hu.piware.bricklog.feature.onboarding.domain.usecase.InitializeChangelogReadVersion
import hu.piware.bricklog.feature.onboarding.presentation.data_fetch.DataFetchViewModel
import hu.piware.bricklog.feature.onboarding.presentation.dispatcher.DispatcherViewModel
import hu.piware.bricklog.feature.set.data.csv.SetListCsvParser
import hu.piware.bricklog.feature.set.data.database.RoomLocalSetDataSource
import hu.piware.bricklog.feature.set.data.database.RoomLocalSetImageDataSource
import hu.piware.bricklog.feature.set.data.database.RoomLocalSetInstructionDataSource
import hu.piware.bricklog.feature.set.data.database.RoomLocalSetPreferencesDataSource
import hu.piware.bricklog.feature.set.data.database.RoomLocalUpdateInfoDataSource
import hu.piware.bricklog.feature.set.data.firebase.FirestoreDataServiceDataSource
import hu.piware.bricklog.feature.set.data.network.KtorRemoteSetDataSource
import hu.piware.bricklog.feature.set.data.network.KtorRemoteSetImageDataSource
import hu.piware.bricklog.feature.set.data.network.KtorRemoteSetInstructionDataSource
import hu.piware.bricklog.feature.set.data.repository.OfflineFirstSetImageRepository
import hu.piware.bricklog.feature.set.data.repository.OfflineFirstSetInstructionRepository
import hu.piware.bricklog.feature.set.data.repository.OfflineFirstSetRepository
import hu.piware.bricklog.feature.set.data.repository.OfflineFirstUpdateInfoRepository
import hu.piware.bricklog.feature.set.data.repository.OfflineSetPreferencesRepository
import hu.piware.bricklog.feature.set.data.repository.RemoteDataServiceRepository
import hu.piware.bricklog.feature.set.domain.datasource.LocalSetDataSource
import hu.piware.bricklog.feature.set.domain.datasource.LocalSetImageDataSource
import hu.piware.bricklog.feature.set.domain.datasource.LocalSetInstructionDataSource
import hu.piware.bricklog.feature.set.domain.datasource.LocalSetPreferencesDataSource
import hu.piware.bricklog.feature.set.domain.datasource.LocalUpdateInfoDataSource
import hu.piware.bricklog.feature.set.domain.datasource.RemoteDataServiceDataSource
import hu.piware.bricklog.feature.set.domain.datasource.RemoteSetDataSource
import hu.piware.bricklog.feature.set.domain.datasource.RemoteSetImageDataSource
import hu.piware.bricklog.feature.set.domain.datasource.RemoteSetInstructionDataSource
import hu.piware.bricklog.feature.set.domain.repository.DataServiceRepository
import hu.piware.bricklog.feature.set.domain.repository.SetImageRepository
import hu.piware.bricklog.feature.set.domain.repository.SetInstructionRepository
import hu.piware.bricklog.feature.set.domain.repository.SetPreferencesRepository
import hu.piware.bricklog.feature.set.domain.repository.SetRepository
import hu.piware.bricklog.feature.set.domain.repository.UpdateInfoRepository
import hu.piware.bricklog.feature.set.domain.usecase.GetAdditionalImages
import hu.piware.bricklog.feature.set.domain.usecase.GetInstructions
import hu.piware.bricklog.feature.set.domain.usecase.GetSets
import hu.piware.bricklog.feature.set.domain.usecase.ResetSets
import hu.piware.bricklog.feature.set.domain.usecase.SendNewSetNotification
import hu.piware.bricklog.feature.set.domain.usecase.ToggleFavouriteSet
import hu.piware.bricklog.feature.set.domain.usecase.UpdateChangelogReadVersion
import hu.piware.bricklog.feature.set.domain.usecase.UpdateSets
import hu.piware.bricklog.feature.set.domain.usecase.WatchBricksetUpdateInfo
import hu.piware.bricklog.feature.set.domain.usecase.WatchFavouriteSetIds
import hu.piware.bricklog.feature.set.domain.usecase.WatchNewChangelog
import hu.piware.bricklog.feature.set.domain.usecase.WatchSetFilterDomain
import hu.piware.bricklog.feature.set.domain.usecase.WatchSetUI
import hu.piware.bricklog.feature.set.domain.usecase.WatchSetUIs
import hu.piware.bricklog.feature.set.domain.usecase.WatchSetsPaged
import hu.piware.bricklog.feature.set.presentation.dashboard.DashboardViewModel
import hu.piware.bricklog.feature.set.presentation.set_detail.SetDetailViewModel
import hu.piware.bricklog.feature.set.presentation.set_image.SetImageViewModel
import hu.piware.bricklog.feature.set.presentation.set_list.SetListViewModel
import hu.piware.bricklog.feature.set.presentation.set_scanner.SetScannerViewModel
import hu.piware.bricklog.feature.settings.data.asset.AssetLocalChangelogDataSource
import hu.piware.bricklog.feature.settings.data.datastore.DataStoreLocalSettingsDataSource
import hu.piware.bricklog.feature.settings.data.repository.OfflineChangelogRepository
import hu.piware.bricklog.feature.settings.data.repository.OfflineSettingsRepository
import hu.piware.bricklog.feature.settings.domain.datasource.LocalChangelogDataSource
import hu.piware.bricklog.feature.settings.domain.datasource.LocalSettingsDataSource
import hu.piware.bricklog.feature.settings.domain.repository.ChangelogRepository
import hu.piware.bricklog.feature.settings.domain.repository.SettingsRepository
import hu.piware.bricklog.feature.settings.domain.usecase.GetChangelog
import hu.piware.bricklog.feature.settings.domain.usecase.ReadTextFile
import hu.piware.bricklog.feature.settings.domain.usecase.SaveNotificationPreferences
import hu.piware.bricklog.feature.settings.domain.usecase.SaveSetFilterPreferences
import hu.piware.bricklog.feature.settings.domain.usecase.SaveSetListDisplayMode
import hu.piware.bricklog.feature.settings.domain.usecase.SaveThemeOption
import hu.piware.bricklog.feature.settings.domain.usecase.WatchNotificationPreferences
import hu.piware.bricklog.feature.settings.domain.usecase.WatchSetFilterPreferences
import hu.piware.bricklog.feature.settings.domain.usecase.WatchSetListDisplayMode
import hu.piware.bricklog.feature.settings.domain.usecase.WatchThemeOption
import hu.piware.bricklog.feature.settings.presentation.appearance.AppearanceViewModel
import hu.piware.bricklog.feature.settings.presentation.changelog.ChangelogViewModel
import hu.piware.bricklog.feature.settings.presentation.license.LicenseViewModel
import hu.piware.bricklog.feature.settings.presentation.notifications.NotificationsViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {
    single(named(HttpClientFactory.DOWNLOAD)) { HttpClientFactory.createDownloadClient(get()) }
    single(named(HttpClientFactory.BRICKSET)) { HttpClientFactory.createBricksetClient(get()) }
    single {
        get<DatabaseFactory>().create().setDriver(BundledSQLiteDriver()).build()
    }
    single {
        get<DatastoreFactory>().create()
    }
    singleOf(::SetListCsvParser)

    single { get<BricklogDatabase>().setDao }
    single { get<BricklogDatabase>().updateInfoDao }
    single { get<BricklogDatabase>().setPreferenceDao }
    single { get<BricklogDatabase>().setImagesDao }
    single { get<BricklogDatabase>().setInstructionsDao }

    singleOf(::RoomLocalSetDataSource).bind<LocalSetDataSource>()
    singleOf(::RoomLocalUpdateInfoDataSource).bind<LocalUpdateInfoDataSource>()
    singleOf(::RoomLocalSetPreferencesDataSource).bind<LocalSetPreferencesDataSource>()
    singleOf(::RoomLocalSetImageDataSource).bind<LocalSetImageDataSource>()
    singleOf(::DataStoreLocalSettingsDataSource).bind<LocalSettingsDataSource>()
    singleOf(::RoomLocalSetInstructionDataSource).bind<LocalSetInstructionDataSource>()
    singleOf(::AssetLocalChangelogDataSource).bind<LocalChangelogDataSource>()

    singleOf(::FirestoreDataServiceDataSource).bind<RemoteDataServiceDataSource>()
    single { KtorRemoteSetDataSource(get(named(HttpClientFactory.DOWNLOAD))) }.bind<RemoteSetDataSource>()
    single { KtorRemoteSetImageDataSource(get(named(HttpClientFactory.BRICKSET))) }.bind<RemoteSetImageDataSource>()
    single { KtorRemoteSetInstructionDataSource(get(named(HttpClientFactory.BRICKSET))) }.bind<RemoteSetInstructionDataSource>()

    singleOf(::RemoteDataServiceRepository).bind<DataServiceRepository>()
    singleOf(::OfflineFirstSetRepository).bind<SetRepository>()
    singleOf(::OfflineFirstUpdateInfoRepository).bind<UpdateInfoRepository>()
    singleOf(::OfflineSetPreferencesRepository).bind<SetPreferencesRepository>()
    singleOf(::OfflineFirstSetImageRepository).bind<SetImageRepository>()
    singleOf(::OfflineSettingsRepository).bind<SettingsRepository>()
    singleOf(::OfflineFirstSetInstructionRepository).bind<SetInstructionRepository>()
    singleOf(::OfflineChangelogRepository).bind<ChangelogRepository>()

    singleOf(::UpdateSets)
    singleOf(::WatchSetUI)
    singleOf(::WatchSetUIs)
    singleOf(::WatchSetsPaged)
    singleOf(::WatchFavouriteSetIds)
    singleOf(::HasAnySets)
    singleOf(::WatchBricksetUpdateInfo)
    singleOf(::ToggleFavouriteSet)
    singleOf(::GetAdditionalImages)
    singleOf(::SaveSetFilterPreferences)
    singleOf(::WatchSetFilterPreferences)
    singleOf(::GetInstructions)
    singleOf(::SaveNotificationPreferences)
    singleOf(::WatchNotificationPreferences)
    singleOf(::ReadTextFile)
    singleOf(::GetChangelog)
    singleOf(::WatchThemeOption)
    singleOf(::SaveThemeOption)
    singleOf(::SaveSetListDisplayMode)
    singleOf(::WatchSetListDisplayMode)
    singleOf(::ResetSets)
    singleOf(::GetSets)
    singleOf(::SendNewSetNotification)
    singleOf(::WatchSetFilterDomain)
    singleOf(::InitializeChangelogReadVersion)
    singleOf(::WatchNewChangelog)
    singleOf(::UpdateChangelogReadVersion)

    viewModelOf(::DispatcherViewModel)
    viewModelOf(::DataFetchViewModel)
    viewModelOf(::SetDetailViewModel)
    viewModelOf(::SetImageViewModel)
    viewModelOf(::DashboardViewModel)
    viewModelOf(::SetListViewModel)
    viewModelOf(::SetScannerViewModel)
    viewModelOf(::NotificationsViewModel)
    viewModelOf(::LicenseViewModel)
    viewModelOf(::ChangelogViewModel)
    viewModelOf(::AppearanceViewModel)
}

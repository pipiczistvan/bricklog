package hu.piware.bricklog.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import hu.piware.bricklog.feature.collection.presentation.collection_edit.CollectionEditViewModel
import hu.piware.bricklog.feature.core.data.database.DatabaseFactory
import hu.piware.bricklog.feature.core.data.datastore.DatastoreFactory
import hu.piware.bricklog.feature.core.data.network.HttpClientFactory
import hu.piware.bricklog.feature.set.presentation.set_detail.SetDetailViewModel
import hu.piware.bricklog.feature.set.presentation.set_image.SetImageViewModel
import hu.piware.bricklog.feature.set.presentation.set_list.SetListViewModel
import hu.piware.bricklog.feature.user.domain.usecase.DeleteUserData
import hu.piware.bricklog.feature.user.domain.usecase.LogOutUser
import io.ktor.client.engine.HttpClientEngine
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

// Only necessary when injecting SavedStateHandle
val viewModelModule = module {
    viewModelOf(::SetDetailViewModel)
    viewModelOf(::SetImageViewModel)
    viewModelOf(::SetListViewModel)
    viewModelOf(::CollectionEditViewModel)
}

// Only necessary when injecting list of dependencies
val useCaseModule = module {
    single { LogOutUser(get(), getAll()) }
    single { DeleteUserData(get(), getAll()) }
}

@Module(
    includes = [
        PlatformModule::class,
        DataModule::class,
        ManagerModule::class,
        UseCaseModule::class,
        ViewModelModule::class,
    ],
)
class AppModule

@Module
expect class PlatformModule()

@Module
@ComponentScan("hu.piware.bricklog.feature.**.data.**")
class DataModule {

    @Single
    @DownloadHttpClient
    fun downloadClient(
        @Provided engine: HttpClientEngine,
    ) =
        HttpClientFactory.createDownloadClient(engine)

    @Single
    @BricksetHttpClient
    fun bricksetClient(
        @Provided engine: HttpClientEngine,
    ) =
        HttpClientFactory.createBricksetClient(engine)

    @Single
    fun bricklogDatabase(
        @Provided factory: DatabaseFactory,
    ) =
        factory.create().setDriver(BundledSQLiteDriver()).build()

    @Single
    fun preferencesDatastore(
        @Provided factory: DatastoreFactory,
    ) = factory.create()
}

@Module
@ComponentScan("hu.piware.bricklog.feature.**.domain.manager")
class ManagerModule

@Module
@ComponentScan("hu.piware.bricklog.feature.**.domain.usecase")
class UseCaseModule

@Module
@ComponentScan("hu.piware.bricklog.feature.**.presentation.**")
class ViewModelModule

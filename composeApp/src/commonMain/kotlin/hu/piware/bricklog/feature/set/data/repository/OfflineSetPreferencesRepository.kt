package hu.piware.bricklog.feature.set.data.repository

import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.set.domain.datasource.LocalSetPreferencesDataSource
import hu.piware.bricklog.feature.set.domain.repository.SetPreferencesRepository
import kotlinx.coroutines.flow.Flow

class OfflineSetPreferencesRepository(
    private val localDataSource: LocalSetPreferencesDataSource,
) : SetPreferencesRepository {

    override fun watchFavouriteSetIds(): Flow<List<Int>> {
        return localDataSource.watchFavouriteSetIds()
    }

    override suspend fun setFavouriteSet(setId: Int, isFavourite: Boolean): EmptyResult<DataError> {
        return localDataSource.setFavouriteSet(setId, isFavourite)
    }

    override fun watchFavouriteSet(setId: Int): Flow<Boolean> {
        return localDataSource.watchFavouriteSet(setId)
    }
}

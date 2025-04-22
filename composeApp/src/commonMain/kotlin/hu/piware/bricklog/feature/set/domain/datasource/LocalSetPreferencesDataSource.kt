package hu.piware.bricklog.feature.set.domain.datasource

import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import kotlinx.coroutines.flow.Flow

interface LocalSetPreferencesDataSource {

    fun watchFavouriteSetIds(): Flow<List<Int>>

    suspend fun setFavouriteSet(setId: Int, isFavourite: Boolean): EmptyResult<DataError.Local>

    fun watchFavouriteSet(setId: Int): Flow<Boolean>
}

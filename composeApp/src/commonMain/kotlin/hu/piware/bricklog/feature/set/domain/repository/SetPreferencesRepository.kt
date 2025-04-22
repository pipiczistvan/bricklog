package hu.piware.bricklog.feature.set.domain.repository

import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import kotlinx.coroutines.flow.Flow

interface SetPreferencesRepository {
    fun watchFavouriteSetIds(): Flow<List<Int>>
    suspend fun setFavouriteSet(setId: Int, isFavourite: Boolean): EmptyResult<DataError>
    fun watchFavouriteSet(setId: Int): Flow<Boolean>
}

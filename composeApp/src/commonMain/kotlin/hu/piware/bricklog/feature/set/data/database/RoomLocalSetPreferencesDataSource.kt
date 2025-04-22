package hu.piware.bricklog.feature.set.data.database

import androidx.sqlite.SQLiteException
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.EmptyResult
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.set.domain.datasource.LocalSetPreferencesDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class RoomLocalSetPreferencesDataSource(
    private val dao: SetPreferenceDao,
) : LocalSetPreferencesDataSource {

    override fun watchFavouriteSetIds(): Flow<List<Int>> {
        return dao.watchFavouriteSets()
            .map { preferences -> preferences.map { it.setId } }
    }

    override suspend fun setFavouriteSet(
        setId: Int,
        isFavourite: Boolean,
    ): EmptyResult<DataError.Local> {
        return try {
            val storedPreference = dao.watchSetPreferenceById(setId).firstOrNull()
            val preferenceToSave = storedPreference
                ?.copy(
                    isFavourite = isFavourite
                )
                ?: SetPreferenceEntity(
                    setId = setId,
                    isFavourite = isFavourite
                )

            dao.upsertSetPreference(preferenceToSave)

            Result.Success(Unit)
        } catch (e: SQLiteException) {
            Result.Error(DataError.Local.DISK_FULL)
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override fun watchFavouriteSet(setId: Int): Flow<Boolean> {
        return dao.watchSetPreferenceById(setId)
            .map { it?.isFavourite ?: false }
    }
}

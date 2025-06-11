package hu.piware.bricklog.feature.set.data.database

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ThemeGroupDao {

    @Query("SELECT * FROM theme_group_view")
    fun watchThemeGroups(): Flow<List<ThemeGroupView>>
}

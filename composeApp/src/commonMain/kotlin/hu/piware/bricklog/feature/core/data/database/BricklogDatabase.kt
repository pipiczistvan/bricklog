package hu.piware.bricklog.feature.core.data.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import hu.piware.bricklog.feature.set.data.database.SetDao
import hu.piware.bricklog.feature.set.data.database.SetEntity
import hu.piware.bricklog.feature.set.data.database.SetImageDao
import hu.piware.bricklog.feature.set.data.database.SetImageEntity
import hu.piware.bricklog.feature.set.data.database.SetInstructionDao
import hu.piware.bricklog.feature.set.data.database.SetInstructionEntity
import hu.piware.bricklog.feature.set.data.database.SetPreferenceDao
import hu.piware.bricklog.feature.set.data.database.SetPreferenceEntity
import hu.piware.bricklog.feature.set.data.database.UpdateInfoDao
import hu.piware.bricklog.feature.set.data.database.UpdateInfoEntity

@Database(
    entities = [
        SetEntity::class,
        SetPreferenceEntity::class,
        UpdateInfoEntity::class,
        SetImageEntity::class,
        SetInstructionEntity::class
    ],
    version = 1
)
@TypeConverters(
    InstantConverter::class
)
@ConstructedBy(BricklogDatabaseConstructor::class)
abstract class BricklogDatabase : RoomDatabase() {
    abstract val setDao: SetDao
    abstract val updateInfoDao: UpdateInfoDao
    abstract val setPreferenceDao: SetPreferenceDao
    abstract val setImagesDao: SetImageDao
    abstract val setInstructionsDao: SetInstructionDao

    companion object {
        const val DB_NAME = "bricklog.db"
    }
}

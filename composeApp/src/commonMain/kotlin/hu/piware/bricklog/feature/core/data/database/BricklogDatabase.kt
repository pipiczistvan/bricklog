package hu.piware.bricklog.feature.core.data.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import hu.piware.bricklog.feature.collection.data.database.CollectionDao
import hu.piware.bricklog.feature.collection.data.database.CollectionEntity
import hu.piware.bricklog.feature.collection.data.database.SetCollectionDao
import hu.piware.bricklog.feature.collection.data.database.SetCollectionEntity
import hu.piware.bricklog.feature.set.data.database.SetDao
import hu.piware.bricklog.feature.set.data.database.SetDetailsDao
import hu.piware.bricklog.feature.set.data.database.SetDetailsView
import hu.piware.bricklog.feature.set.data.database.SetEntity
import hu.piware.bricklog.feature.set.data.database.SetImageDao
import hu.piware.bricklog.feature.set.data.database.SetImageEntity
import hu.piware.bricklog.feature.set.data.database.SetInstructionDao
import hu.piware.bricklog.feature.set.data.database.SetInstructionEntity
import hu.piware.bricklog.feature.set.data.database.ThemeGroupDao
import hu.piware.bricklog.feature.set.data.database.ThemeGroupView
import hu.piware.bricklog.feature.set.data.database.UpdateInfoDao
import hu.piware.bricklog.feature.set.data.database.UpdateInfoEntity

@Database(
    entities = [
        SetEntity::class,
        UpdateInfoEntity::class,
        SetImageEntity::class,
        SetInstructionEntity::class,
        CollectionEntity::class,
        SetCollectionEntity::class
    ],
    views = [
        SetDetailsView::class,
        ThemeGroupView::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    InstantConverter::class
)
@ConstructedBy(BricklogDatabaseConstructor::class)
abstract class BricklogDatabase : RoomDatabase() {
    abstract val setDao: SetDao
    abstract val setDetailsDao: SetDetailsDao
    abstract val updateInfoDao: UpdateInfoDao
    abstract val setImagesDao: SetImageDao
    abstract val setInstructionsDao: SetInstructionDao
    abstract val collectionDao: CollectionDao
    abstract val setCollectionDao: SetCollectionDao
    abstract val themeGroupDao: ThemeGroupDao

    companion object {
        const val DB_NAME = "bricklog.db"
    }
}

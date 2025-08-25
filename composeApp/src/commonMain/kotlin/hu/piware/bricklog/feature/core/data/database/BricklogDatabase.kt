package hu.piware.bricklog.feature.core.data.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import hu.piware.bricklog.feature.collection.data.database.CollectionDao
import hu.piware.bricklog.feature.collection.data.database.CollectionEntity
import hu.piware.bricklog.feature.collection.data.database.CollectionSetDao
import hu.piware.bricklog.feature.collection.data.database.CollectionSetEntity
import hu.piware.bricklog.feature.collection.data.database.CollectionShareEntity
import hu.piware.bricklog.feature.collection.data.database.CollectionWithSetIdDao
import hu.piware.bricklog.feature.currency.data.database.CurrencyRateDao
import hu.piware.bricklog.feature.currency.data.database.CurrencyRateEntity
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
import hu.piware.bricklog.feature.user.data.database.FriendDao
import hu.piware.bricklog.feature.user.data.database.FriendEntity
import hu.piware.bricklog.feature.user.data.database.UserPreferencesDao
import hu.piware.bricklog.feature.user.data.database.UserPreferencesEntity

@Database(
    entities = [
        SetEntity::class,
        UpdateInfoEntity::class,
        SetImageEntity::class,
        SetInstructionEntity::class,
        CollectionEntity::class,
        CollectionShareEntity::class,
        CollectionSetEntity::class,
        UserPreferencesEntity::class,
        CurrencyRateEntity::class,
        FriendEntity::class,
    ],
    views = [
        SetDetailsView::class,
        ThemeGroupView::class,
    ],
    version = 1,
    exportSchema = false,
)
@TypeConverters(
    InstantConverter::class,
    StringListConverter::class,
)
@ConstructedBy(BricklogDatabaseConstructor::class)
abstract class BricklogDatabase : RoomDatabase() {
    abstract val setDao: SetDao
    abstract val setDetailsDao: SetDetailsDao
    abstract val updateInfoDao: UpdateInfoDao
    abstract val setImagesDao: SetImageDao
    abstract val setInstructionsDao: SetInstructionDao
    abstract val collectionDao: CollectionDao
    abstract val collectionWithSetIdDao: CollectionWithSetIdDao
    abstract val collectionSetDao: CollectionSetDao
    abstract val themeGroupDao: ThemeGroupDao
    abstract val userPreferencesDao: UserPreferencesDao
    abstract val currencyRateDao: CurrencyRateDao
    abstract val friendDao: FriendDao

    companion object {
        const val DB_NAME = "bricklog.db"
    }
}

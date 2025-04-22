package hu.piware.bricklog.feature.core.data.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

actual class DatabaseFactory(
    private val context: Context
) {
    actual fun create(): RoomDatabase.Builder<BricklogDatabase> {
        val appContext = context.applicationContext
        val dbFile = appContext.getDatabasePath(BricklogDatabase.DB_NAME)

        return Room.databaseBuilder<BricklogDatabase>(
            context = context.applicationContext,
            name = dbFile.absolutePath
        )
    }
}

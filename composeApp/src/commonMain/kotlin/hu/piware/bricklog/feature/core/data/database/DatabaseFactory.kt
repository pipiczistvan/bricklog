package hu.piware.bricklog.feature.core.data.database

import androidx.room.RoomDatabase

expect class DatabaseFactory {
    fun create(): RoomDatabase.Builder<BricklogDatabase>
}

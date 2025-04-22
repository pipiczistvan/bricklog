package hu.piware.bricklog.feature.core.data.database

import androidx.room.RoomDatabaseConstructor

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object BricklogDatabaseConstructor : RoomDatabaseConstructor<BricklogDatabase> {
    override fun initialize(): BricklogDatabase
}

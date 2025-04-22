package hu.piware.bricklog.feature.set.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "set_preferences")
data class SetPreferenceEntity(
    @PrimaryKey(autoGenerate = false) val setId: Int,
    val isFavourite: Boolean
)

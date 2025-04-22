package hu.piware.bricklog.feature.set.data.database

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "set_images",
    indices = [
        Index(value = ["setId"])
    ]
)
data class SetImageEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val setId: Int,
    val thumbnailURL: String?,
    val imageURL: String?
)

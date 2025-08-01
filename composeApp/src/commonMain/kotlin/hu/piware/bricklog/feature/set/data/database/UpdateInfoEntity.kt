package hu.piware.bricklog.feature.set.data.database

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import hu.piware.bricklog.feature.set.domain.model.DataType
import kotlinx.datetime.Instant

@Entity(
    tableName = "update_info",
    indices = [
        Index(value = ["setId"]),
    ],
)
data class UpdateInfoEntity(
    @PrimaryKey(autoGenerate = false) val dataType: DataType,
    val setId: Int?,
    val lastUpdated: Instant,
)

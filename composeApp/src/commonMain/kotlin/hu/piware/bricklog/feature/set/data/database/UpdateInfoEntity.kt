package hu.piware.bricklog.feature.set.data.database

import androidx.room.Entity
import hu.piware.bricklog.feature.set.domain.model.DataType
import kotlinx.datetime.Instant

@Entity(
    tableName = "update_info",
    primaryKeys = ["dataType"]
)
data class UpdateInfoEntity(
    val dataType: DataType,
    val lastUpdated: Instant
)

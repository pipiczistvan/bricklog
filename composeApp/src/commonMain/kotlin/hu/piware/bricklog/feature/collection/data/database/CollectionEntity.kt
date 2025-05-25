package hu.piware.bricklog.feature.collection.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import hu.piware.bricklog.feature.collection.domain.model.CollectionIcon
import hu.piware.bricklog.feature.collection.domain.model.CollectionId

@Entity(
    tableName = "collections"
)
data class CollectionEntity(
    @PrimaryKey(autoGenerate = true) val id: CollectionId = 0,
    val name: String,
    val icon: CollectionIcon,
)

package hu.piware.bricklog.feature.collection.data.database

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import hu.piware.bricklog.feature.collection.domain.model.CollectionIcon
import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.collection.domain.model.CollectionType
import hu.piware.bricklog.feature.user.domain.model.UserId

@Entity(
    tableName = "collections",
    indices = [
        Index("owner"),
    ],
)
data class CollectionEntity(
    @PrimaryKey val id: CollectionId,
    val owner: UserId,
    val name: String,
    val icon: CollectionIcon,
    val type: CollectionType,
)

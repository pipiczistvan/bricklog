package hu.piware.bricklog.feature.collection.data.database

import androidx.room.ColumnInfo
import androidx.room.Embedded
import hu.piware.bricklog.feature.set.domain.model.SetId

data class CollectionWithSetId(
    @Embedded val collection: CollectionEntity,
    @ColumnInfo(name = "setId") val setId: SetId,
)

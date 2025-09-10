package hu.piware.bricklog.feature.set.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import hu.piware.bricklog.feature.set.domain.model.SetId

@Entity(
    tableName = "cmf_codes",
)
data class CmfCodeEntity(
    @PrimaryKey val code: String,
    val setId: SetId,
    val seriesSetId: SetId,
    val manufacturerId: String,
)

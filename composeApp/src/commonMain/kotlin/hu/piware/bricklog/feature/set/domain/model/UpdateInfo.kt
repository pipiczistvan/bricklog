package hu.piware.bricklog.feature.set.domain.model

import kotlinx.datetime.Instant

data class UpdateInfo(
    val dataType: DataType,
    val setId: Int? = null,
    val lastUpdated: Instant,
)

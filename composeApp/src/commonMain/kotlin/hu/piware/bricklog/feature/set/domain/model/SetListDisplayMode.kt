package hu.piware.bricklog.feature.set.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class SetListDisplayMode {
    COLUMN,
    GRID,
    GRID_LARGE
}

package hu.piware.bricklog.feature.set.presentation.set_detail

import kotlinx.serialization.Serializable

@Serializable
data class SetDetailArguments(
    val setId: Int,
    val sharedElementPrefix: String,
)

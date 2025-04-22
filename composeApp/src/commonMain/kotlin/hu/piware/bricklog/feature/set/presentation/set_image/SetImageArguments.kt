package hu.piware.bricklog.feature.set.presentation.set_image

import kotlinx.serialization.Serializable

@Serializable
data class SetImageArguments(
    val setId: Int,
    val sharedElementPrefix: String,
)

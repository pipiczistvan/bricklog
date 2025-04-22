package hu.piware.bricklog.feature.set.presentation.set_image

import hu.piware.bricklog.feature.set.domain.model.Image

data class SetImageState(
    val setId: Int,
    val sharedElementPrefix: String = "",
    val images: List<Image> = emptyList(),
)

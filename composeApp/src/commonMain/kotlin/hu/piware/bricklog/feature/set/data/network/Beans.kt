package hu.piware.bricklog.feature.set.data.network

import kotlinx.serialization.Serializable

@Serializable
enum class BricksetStatus {
    success, error
}

@Serializable
data class BricksetAdditionalImagesDto(
    val status: BricksetStatus? = null,
    val message: String? = null,
    val matches: Int? = null,
    val additionalImages: List<ImageDto> = emptyList()
)

@Serializable
data class ImageDto(
    var thumbnailURL: String? = null,
    var imageURL: String? = null
)

@Serializable
data class BricksetInstructionsDto(
    val status: BricksetStatus? = null,
    val message: String? = null,
    val matches: Int? = null,
    val instructions: List<InstructionDto> = emptyList()
)

@Serializable
data class InstructionDto(
    val URL: String? = null,
    val description: String? = null
)

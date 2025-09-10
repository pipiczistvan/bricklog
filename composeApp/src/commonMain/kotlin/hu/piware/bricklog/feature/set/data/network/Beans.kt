package hu.piware.bricklog.feature.set.data.network

import hu.piware.bricklog.feature.set.domain.model.SetId
import kotlinx.serialization.Serializable

@Serializable
enum class BricksetStatus {
    success,
    error,
}

@Serializable
data class BricksetAdditionalImagesDto(
    val status: BricksetStatus? = null,
    val message: String? = null,
    val matches: Int? = null,
    val additionalImages: List<ImageDto> = emptyList(),
)

@Serializable
data class ImageDto(
    var thumbnailURL: String? = null,
    var imageURL: String? = null,
)

@Serializable
data class BricksetInstructionsDto(
    val status: BricksetStatus? = null,
    val message: String? = null,
    val matches: Int? = null,
    val instructions: List<InstructionDto> = emptyList(),
)

@Serializable
data class InstructionDto(
    val URL: String? = null,
    val description: String? = null,
)

@Serializable
data class CmfSeriesDto(
    val setNum: String,
    val setId: SetId,
    val setName: String,
    val codes: List<CmfCodeListDto>,
)

@Serializable
data class CmfCodeListDto(
    val setId: SetId,
    val r: List<String>,
    val s: List<String>,
)

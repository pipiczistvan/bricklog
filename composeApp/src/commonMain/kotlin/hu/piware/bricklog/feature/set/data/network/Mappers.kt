package hu.piware.bricklog.feature.set.data.network

import hu.piware.bricklog.feature.set.domain.model.CmfCode
import hu.piware.bricklog.feature.set.domain.model.Image
import hu.piware.bricklog.feature.set.domain.model.Instruction
import hu.piware.bricklog.feature.set.domain.model.SetId

fun ImageDto.toDomainModel(): Image {
    return Image(
        thumbnailURL = thumbnailURL,
        imageURL = imageURL,
    )
}

fun InstructionDto.toDomainModel(): Instruction {
    return Instruction(
        URL = URL,
        description = description,
    )
}

fun CmfCodeListDto.toDomainModels(seriesSetId: SetId): List<CmfCode> {
    val mexicanCodes = r.map {
        CmfCode(
            code = it,
            setId = setId,
            seriesSetId = seriesSetId,
            manufacturerId = "R",
        )
    }
    val czechCodes = s.map {
        CmfCode(
            code = it,
            setId = setId,
            seriesSetId = seriesSetId,
            manufacturerId = "S",
        )
    }

    return mexicanCodes + czechCodes
}

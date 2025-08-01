package hu.piware.bricklog.feature.set.data.network

import hu.piware.bricklog.feature.set.domain.model.Image
import hu.piware.bricklog.feature.set.domain.model.Instruction

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

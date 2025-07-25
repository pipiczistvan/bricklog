package hu.piware.bricklog.feature.set.data.database

import hu.piware.bricklog.feature.collection.data.database.toDomainModel
import hu.piware.bricklog.feature.set.domain.model.Image
import hu.piware.bricklog.feature.set.domain.model.Instruction
import hu.piware.bricklog.feature.set.domain.model.Set
import hu.piware.bricklog.feature.set.domain.model.SetDetails
import hu.piware.bricklog.feature.set.domain.model.UpdateInfo
import hu.piware.bricklog.feature.user.domain.model.UserId

fun Set.toEntity(): SetEntity {
    return SetEntity(
        id = setID,
        number = number,
        numberVariant = numberVariant,
        name = name,
        year = year,
        theme = theme,
        themeGroup = themeGroup,
        subTheme = subTheme,
        category = category,
        pieces = pieces,
        minifigs = minifigs,
        thumbnailURL = image.thumbnailURL,
        imageURL = image.imageURL,
        bricksetURL = bricksetURL,
        USPrice = USPrice,
        DEPrice = DEPrice,
        launchDate = launchDate,
        exitDate = exitDate,
        packagingType = packagingType,
        availability = availability,
        ageMin = ageMin,
        ageMax = ageMax,
        height = height,
        width = width,
        depth = depth,
        weight = weight,
        barcodeEAN = barcodeEAN,
        barcodeUPC = barcodeUPC,
        lastUpdated = lastUpdated,
        infoCompleteDate = infoCompleteDate
    )
}

fun SetEntity.toDomainModel(): Set {
    return Set(
        setID = id,
        number = number,
        numberVariant = numberVariant,
        name = name,
        year = year,
        theme = theme,
        themeGroup = themeGroup,
        subTheme = subTheme,
        category = category,
        pieces = pieces,
        minifigs = minifigs,
        image = Image(
            thumbnailURL = thumbnailURL,
            imageURL = imageURL
        ),
        bricksetURL = bricksetURL,
        USPrice = USPrice,
        DEPrice = DEPrice,
        launchDate = launchDate,
        exitDate = exitDate,
        packagingType = packagingType,
        availability = availability,
        ageMin = ageMin,
        ageMax = ageMax,
        height = height,
        width = width,
        depth = depth,
        weight = weight,
        barcodeEAN = barcodeEAN,
        barcodeUPC = barcodeUPC,
        lastUpdated = lastUpdated,
        infoCompleteDate = infoCompleteDate
    )
}

fun SetDetailsView.toDomainModel(userId: UserId): SetDetails {
    return SetDetails(
        set = legoSet.toDomainModel(),
        collections = collections.filter { it.userId == userId }.map { it.toDomainModel() },
        status = status
    )
}

fun UpdateInfoEntity.toDomainModel(): UpdateInfo {
    return UpdateInfo(
        dataType = dataType,
        setId = setId,
        lastUpdated = lastUpdated
    )
}

fun UpdateInfo.toEntity(): UpdateInfoEntity {
    return UpdateInfoEntity(
        dataType = dataType,
        setId = setId,
        lastUpdated = lastUpdated
    )
}

fun SetImageEntity.toDomainModel(): Image {
    return Image(
        thumbnailURL = thumbnailURL,
        imageURL = imageURL
    )
}

fun Image.toEntity(setId: Int): SetImageEntity {
    return SetImageEntity(
        setId = setId,
        thumbnailURL = thumbnailURL,
        imageURL = imageURL
    )
}

fun SetInstructionEntity.toDomainModel(): Instruction {
    return Instruction(
        URL = URL,
        description = description
    )
}

fun Instruction.toEntity(setId: Int): SetInstructionEntity {
    return SetInstructionEntity(
        setId = setId,
        URL = URL,
        description = description
    )
}

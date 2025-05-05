package hu.piware.bricklog.feature.set.data.csv

import hu.piware.bricklog.feature.set.domain.model.Image
import hu.piware.bricklog.feature.set.domain.model.Set

fun SetRow.toDomainModel(): Set {
    return Set(
        setID = setID,
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
            thumbnailURL = thumbnailURL?.let { "https://images.brickset.com/sets/small/$it" },
            imageURL = imageURL?.let { "https://images.brickset.com/sets/images/$it" }
        ),
        bricksetURL = bricksetURL?.let { "https://brickset.com/sets/$it" },
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

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
        released = released,
        pieces = pieces,
        minifigs = minifigs,
        image = Image(
            thumbnailURL = thumbnailURL,
            imageURL = imageURL
        ),
        bricksetURL = bricksetURL,
        USPrice = USPrice,
        UKPrice = UKPrice,
        CAPrice = CAPrice,
        DEPrice = DEPrice,
        rating = rating,
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
        lastUpdated = lastUpdated
    )
}

package hu.piware.bricklog.feature.set.data.database

import hu.piware.bricklog.feature.collection.data.database.toDomainModel
import hu.piware.bricklog.feature.currency.domain.model.CurrencyRegion
import hu.piware.bricklog.feature.set.domain.model.Image
import hu.piware.bricklog.feature.set.domain.model.Instruction
import hu.piware.bricklog.feature.set.domain.model.PriceFilterOption
import hu.piware.bricklog.feature.set.domain.model.Set
import hu.piware.bricklog.feature.set.domain.model.SetDetails
import hu.piware.bricklog.feature.set.domain.model.SetPriceCategory
import hu.piware.bricklog.feature.set.domain.model.SetQueryOptions
import hu.piware.bricklog.feature.set.domain.model.UpdateInfo

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
        infoCompleteDate = infoCompleteDate,
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
            imageURL = imageURL,
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
        infoCompleteDate = infoCompleteDate,
    )
}

fun SetDetailsView.toDomainModel(queryOptions: SetQueryOptions): SetDetails {
    return SetDetails(
        set = legoSet.toDomainModel(),
        collections = collections.filter { it.userId == queryOptions.userId }
            .map { it.toDomainModel() },
        status = status,
        priceCategory = priceCategory(queryOptions),
    )
}

private fun SetDetailsView.priceCategory(queryOptions: SetQueryOptions): SetPriceCategory {
    return with(queryOptions.currencyDetails) {
        when (preferredRegion) {
            CurrencyRegion.EU -> legoSet.DEPrice.priceCategory(priceRanges)
            CurrencyRegion.US -> legoSet.USPrice.priceCategory(priceRanges)
        } ?: SetPriceCategory.UNKNOWN
    }
}

private fun Double?.priceCategory(priceRanges: Map<PriceFilterOption, Pair<Double?, Double?>>): SetPriceCategory? {
    if (this == null) return null

    return if (priceRanges[PriceFilterOption.BUDGET].contains(this)) {
        SetPriceCategory.BUDGET
    } else if (priceRanges[PriceFilterOption.AFFORDABLE].contains(this)) {
        SetPriceCategory.AFFORDABLE
    } else if (priceRanges[PriceFilterOption.EXPENSIVE].contains(this)) {
        SetPriceCategory.EXPENSIVE
    } else if (priceRanges[PriceFilterOption.PREMIUM].contains(this)) {
        SetPriceCategory.PREMIUM
    } else {
        null
    }
}

private fun Pair<Double?, Double?>?.contains(value: Double): Boolean {
    val first = this?.first
    val second = this?.second

    return (first == null || first <= value) && (second == null || second >= value)
}

fun UpdateInfoEntity.toDomainModel(): UpdateInfo {
    return UpdateInfo(
        dataType = dataType,
        setId = setId,
        lastUpdated = lastUpdated,
    )
}

fun UpdateInfo.toEntity(): UpdateInfoEntity {
    return UpdateInfoEntity(
        dataType = dataType,
        setId = setId,
        lastUpdated = lastUpdated,
    )
}

fun SetImageEntity.toDomainModel(): Image {
    return Image(
        thumbnailURL = thumbnailURL,
        imageURL = imageURL,
    )
}

fun Image.toEntity(setId: Int): SetImageEntity {
    return SetImageEntity(
        setId = setId,
        thumbnailURL = thumbnailURL,
        imageURL = imageURL,
    )
}

fun SetInstructionEntity.toDomainModel(): Instruction {
    return Instruction(
        URL = URL,
        description = description,
    )
}

fun Instruction.toEntity(setId: Int): SetInstructionEntity {
    return SetInstructionEntity(
        setId = setId,
        URL = URL,
        description = description,
    )
}

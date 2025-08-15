package hu.piware.bricklog.feature.set.domain.util

import hu.piware.bricklog.feature.currency.domain.model.CurrencyPreferenceDetails
import hu.piware.bricklog.feature.currency.domain.model.CurrencyRegion
import hu.piware.bricklog.feature.set.domain.model.EUPrice
import hu.piware.bricklog.feature.set.domain.model.SetDetails
import hu.piware.bricklog.feature.set.domain.model.SetPriceDetails
import hu.piware.bricklog.feature.set.domain.model.USPrice

fun combineSetWithCurrencyPreference(
    setDetails: SetDetails?,
    currencyDetails: CurrencyPreferenceDetails?,
): SetPriceDetails? {
    if (setDetails == null || currencyDetails == null) {
        return null
    }

    val setPriceInEur = calculateSetPriceInEur(setDetails, currencyDetails)
    val setPriceInTargetCurrency =
        eurPriceInCurrency(setPriceInEur, currencyDetails.preferredCurrencyEurRate)

    val setPriceInPreferredRegion = when (currencyDetails.preferredRegion) {
        CurrencyRegion.EU -> setDetails.EUPrice
        CurrencyRegion.US -> setDetails.USPrice
    }

    if (setPriceInTargetCurrency == null || setPriceInPreferredRegion == null) {
        return null
    }

    return SetPriceDetails(
        price = setPriceInTargetCurrency,
        currencyCode = currencyDetails.preferredCurrencyCode,
        regionPrice = setPriceInPreferredRegion,
    )
}

fun regionPriceInCurrency(
    regionPrice: Double?,
    currencyDetails: CurrencyPreferenceDetails,
): Double? {
    val regionPriceInEur = when (currencyDetails.preferredRegion) {
        CurrencyRegion.EU -> regionPrice
        CurrencyRegion.US -> usdPriceInEur(regionPrice, currencyDetails.usdEurRate)
    }

    return eurPriceInCurrency(regionPriceInEur, currencyDetails.preferredCurrencyEurRate)
}

fun currencyPriceInRegion(
    currencyPrice: Double?,
    currencyDetails: CurrencyPreferenceDetails,
): Double? {
    val currencyPriceInEur =
        currencyPriceInEur(currencyPrice, currencyDetails.preferredCurrencyEurRate)

    return when (currencyDetails.preferredRegion) {
        CurrencyRegion.EU -> currencyPriceInEur
        CurrencyRegion.US -> eurPriceInUsd(currencyPriceInEur, currencyDetails.usdEurRate)
    }
}

private fun calculateSetPriceInEur(
    setDetails: SetDetails,
    currencyDetails: CurrencyPreferenceDetails,
): Double? {
    return when (currencyDetails.preferredRegion) {
        CurrencyRegion.EU -> setDetails.EUPrice
        CurrencyRegion.US -> usdPriceInEur(setDetails.USPrice, currencyDetails.usdEurRate)
    }
}

private fun eurPriceInUsd(priceInEur: Double?, usdRate: Double?) =
    eurPriceInCurrency(priceInEur, usdRate)

private fun usdPriceInEur(priceInUsd: Double?, usdRate: Double?) =
    currencyPriceInEur(priceInUsd, usdRate)

private fun eurPriceInCurrency(priceInEur: Double?, rate: Double?) =
    if (priceInEur != null && rate != null) {
        priceInEur * rate
    } else {
        null
    }

private fun currencyPriceInEur(priceInCurrency: Double?, rate: Double?) =
    if (priceInCurrency != null && rate != null) {
        priceInCurrency / rate
    } else {
        null
    }

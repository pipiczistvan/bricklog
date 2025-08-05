package hu.piware.bricklog.feature.set.domain.util

import hu.piware.bricklog.feature.currency.domain.model.CurrencyPreferenceDetails
import hu.piware.bricklog.feature.currency.domain.model.CurrencyRegion
import hu.piware.bricklog.feature.set.domain.model.SetDetails
import hu.piware.bricklog.feature.set.domain.model.SetPriceDetails

fun combineSetWithCurrencyPreference(
    setDetails: SetDetails?,
    currencyDetails: CurrencyPreferenceDetails?,
): SetPriceDetails? {
    if (setDetails == null || currencyDetails == null) {
        return null
    }

    val setPriceInEur = calculateSetPriceInEur(setDetails, currencyDetails)
    val setPriceInTargetCurrency =
        priceInOtherCurrency(setPriceInEur, currencyDetails.preferredCurrencyEurRate)

    return setPriceInTargetCurrency?.let {
        SetPriceDetails(
            price = setPriceInTargetCurrency,
            currencyCode = currencyDetails.preferredCurrencyCode,
        )
    }
}

private fun calculateSetPriceInEur(
    setDetails: SetDetails,
    currencyDetails: CurrencyPreferenceDetails,
): Double? {
    return when (currencyDetails.preferredRegion) {
        CurrencyRegion.EU -> setDetails.eurPrice()
        CurrencyRegion.US -> setDetails.usPriceInEur(currencyDetails.usdEurRate)
    } ?: when (currencyDetails.preferredRegion) { // Fallback to other region
        CurrencyRegion.EU -> setDetails.usPriceInEur(currencyDetails.usdEurRate)
        CurrencyRegion.US -> setDetails.eurPrice()
    }
}

private fun SetDetails.eurPrice() = this.set.DEPrice

private fun SetDetails.usPriceInEur(usdRate: Double?) =
    if (this.set.USPrice != null && usdRate != null) {
        this.set.USPrice / usdRate
    } else {
        null
    }

private fun priceInOtherCurrency(priceInEur: Double?, rate: Double?) =
    if (priceInEur != null && rate != null) {
        priceInEur * rate
    } else {
        null
    }

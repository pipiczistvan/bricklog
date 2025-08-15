package hu.piware.bricklog.feature.set.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.feature_set_search_price_filter_btn_any
import hu.piware.bricklog.feature.core.presentation.LocaleManager
import hu.piware.bricklog.feature.currency.domain.model.CurrencyPreferenceDetails
import hu.piware.bricklog.feature.currency.domain.util.CurrencyFormatter
import hu.piware.bricklog.feature.set.domain.model.SetPriceDetails
import hu.piware.bricklog.feature.set.domain.util.regionPriceInCurrency
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun formattedPrice(priceDetails: SetPriceDetails): String {
    return formattedPrice(priceDetails.price, priceDetails.currencyCode)
}

@Composable
fun formattedPrice(price: Double, currencyCode: String): String {
    val localeManager = koinInject<LocaleManager>()

    return CurrencyFormatter.formatCurrency(
        amount = price,
        currencyCode = currencyCode,
        locale = localeManager.getCurrentLocale(),
    )
}

@Composable
fun formatedPriceRange(
    min: Double?,
    max: Double?,
    currencyDetails: CurrencyPreferenceDetails,
): String {
    val currencyMin = remember(min, currencyDetails) { regionPriceInCurrency(min, currencyDetails) }
    val currencyMax = remember(max, currencyDetails) { regionPriceInCurrency(max, currencyDetails) }

    return if (currencyMin != null && currencyMax != null) {
        formattedPrice(currencyMin, currencyDetails.preferredCurrencyCode) +
                " - " +
                formattedPrice(currencyMax, currencyDetails.preferredCurrencyCode)
    } else if (currencyMin != null) {
        ">= ${formattedPrice(currencyMin, currencyDetails.preferredCurrencyCode)}"
    } else if (currencyMax != null) {
        "<= ${formattedPrice(currencyMax, currencyDetails.preferredCurrencyCode)}"
    } else {
        stringResource(Res.string.feature_set_search_price_filter_btn_any)
    }
}

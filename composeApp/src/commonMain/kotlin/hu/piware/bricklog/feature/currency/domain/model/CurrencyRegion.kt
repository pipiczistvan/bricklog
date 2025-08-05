package hu.piware.bricklog.feature.currency.domain.model

import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.feature_currency_region_name_eu
import bricklog.composeapp.generated.resources.feature_currency_region_name_us
import hu.piware.bricklog.feature.core.presentation.UiText
import hu.piware.bricklog.feature.currency.domain.util.CURRENCY_CODE_EUR
import hu.piware.bricklog.feature.currency.domain.util.CURRENCY_CODE_USD

enum class CurrencyRegion {
    EU,
    US,
}

val CurrencyRegion.code: String
    get() = when (this) {
        CurrencyRegion.EU -> CURRENCY_CODE_EUR
        CurrencyRegion.US -> CURRENCY_CODE_USD
    }

fun CurrencyRegion.toUiText(): UiText {
    return when (this) {
        CurrencyRegion.EU -> UiText.StringResourceId(Res.string.feature_currency_region_name_eu)
        CurrencyRegion.US -> UiText.StringResourceId(Res.string.feature_currency_region_name_us)
    }
}

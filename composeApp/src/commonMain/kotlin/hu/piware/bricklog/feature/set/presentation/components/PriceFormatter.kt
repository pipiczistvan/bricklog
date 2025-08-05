package hu.piware.bricklog.feature.set.presentation.components

import androidx.compose.runtime.Composable
import hu.piware.bricklog.feature.core.presentation.LocaleManager
import hu.piware.bricklog.feature.currency.domain.util.CurrencyFormatter
import hu.piware.bricklog.feature.set.domain.model.SetPriceDetails
import org.koin.compose.koinInject

@Composable
fun formattedPrice(priceDetails: SetPriceDetails): String {
    val localeManager = koinInject<LocaleManager>()

    return CurrencyFormatter.formatCurrency(
        amount = priceDetails.price,
        currencyCode = priceDetails.currencyCode,
        locale = localeManager.getCurrentLocale(),
    )
}

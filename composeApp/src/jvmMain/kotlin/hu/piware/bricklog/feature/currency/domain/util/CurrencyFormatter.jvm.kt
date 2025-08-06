package hu.piware.bricklog.feature.currency.domain.util

import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

actual object CurrencyFormatter {

    actual fun formatCurrency(
        amount: Double,
        currencyCode: String,
        locale: String,
    ): String {
        val formatter =
            NumberFormat.getCurrencyInstance(Locale.forLanguageTag(locale))
        val currency = Currency.getInstance(currencyCode.uppercase())
        formatter.currency = currency
        formatter.minimumFractionDigits = 0
        formatter.maximumFractionDigits = currency.defaultFractionDigits
        return formatter.format(amount)
    }
}

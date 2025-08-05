package hu.piware.bricklog.feature.currency.domain.util

import platform.Foundation.NSLocale
import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter
import platform.Foundation.NSNumberFormatterCurrencyStyle
import platform.Foundation.localeWithLocaleIdentifier

actual object CurrencyFormatter {

    actual fun formatCurrency(
        amount: Double,
        currencyCode: String,
        locale: String,
    ): String {
        val formatter = NSNumberFormatter().apply {
            numberStyle = NSNumberFormatterCurrencyStyle
            this.currencyCode = currencyCode
            this.locale = NSLocale.localeWithLocaleIdentifier(locale)
            minimumFractionDigits = 0u
        }
        return formatter.stringFromNumber(NSNumber(amount)) ?: "$currencyCode $amount"
    }
}

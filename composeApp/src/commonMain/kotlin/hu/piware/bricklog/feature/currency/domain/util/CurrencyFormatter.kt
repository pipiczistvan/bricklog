package hu.piware.bricklog.feature.currency.domain.util

expect object CurrencyFormatter {
    fun formatCurrency(amount: Double, currencyCode: String, locale: String): String
}

package hu.piware.bricklog.feature.set.domain.model

import hu.piware.bricklog.feature.collection.domain.model.Collection
import hu.piware.bricklog.feature.currency.domain.model.CurrencyPreferenceDetails

data class SetFilterDomain(
    val themes: List<String> = emptyList(),
    val packagingTypes: List<String> = emptyList(),
    val collections: List<Collection> = emptyList(),
    val currencyDetails: CurrencyPreferenceDetails? = null,
)

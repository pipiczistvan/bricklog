package hu.piware.bricklog.feature.set.domain.model

import hu.piware.bricklog.feature.collection.domain.model.Collection

data class SetFilterDomain(
    val themes: List<String> = emptyList(),
    val packagingTypes: List<String> = emptyList(),
    val collections: List<Collection> = emptyList(),
)

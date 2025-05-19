package hu.piware.bricklog.feature.set.domain.model

data class SetFilterDomain(
    val themes: List<String> = emptyList(),
    val packagingTypes: List<String> = emptyList(),
)

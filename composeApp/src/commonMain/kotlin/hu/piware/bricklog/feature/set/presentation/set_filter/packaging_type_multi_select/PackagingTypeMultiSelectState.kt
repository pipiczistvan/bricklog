package hu.piware.bricklog.feature.set.presentation.set_filter.packaging_type_multi_select

data class PackagingTypeMultiSelectState(
    val availablePackagingTypes: List<String> = emptyList(),
    val selectedPackagingTypes: Set<String> = emptySet(),
    val searchQuery: String = "",
)

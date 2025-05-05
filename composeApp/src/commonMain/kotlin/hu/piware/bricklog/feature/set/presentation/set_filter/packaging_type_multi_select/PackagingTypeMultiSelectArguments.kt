package hu.piware.bricklog.feature.set.presentation.set_filter.packaging_type_multi_select

import kotlinx.serialization.Serializable

@Serializable
data class PackagingTypeMultiSelectArguments(
    val packagingTypes: Set<String>,
)

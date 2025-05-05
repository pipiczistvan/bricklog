package hu.piware.bricklog.feature.set.presentation.set_filter.theme_multi_select

import kotlinx.serialization.Serializable

@Serializable
data class ThemeMultiSelectArguments(
    val themes: Set<String>,
)

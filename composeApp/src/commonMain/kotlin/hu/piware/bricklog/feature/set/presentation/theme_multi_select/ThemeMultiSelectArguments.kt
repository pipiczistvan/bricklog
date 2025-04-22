package hu.piware.bricklog.feature.set.presentation.theme_multi_select

import kotlinx.serialization.Serializable

@Serializable
data class ThemeMultiSelectArguments(
    val themes: Set<String>,
)

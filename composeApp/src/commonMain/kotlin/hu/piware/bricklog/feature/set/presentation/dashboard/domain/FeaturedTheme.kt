package hu.piware.bricklog.feature.set.presentation.dashboard.domain

import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

data class FeaturedTheme(
    val imageResId: DrawableResource,
    val contentDescriptionResId: StringResource,
    val color: Color,
    val theme: String,
)

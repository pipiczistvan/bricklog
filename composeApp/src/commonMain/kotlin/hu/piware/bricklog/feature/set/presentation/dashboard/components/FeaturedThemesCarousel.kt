@file:OptIn(ExperimentalMaterial3Api::class)

package hu.piware.bricklog.feature.set.presentation.dashboard.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.CarouselDefaults
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import hu.piware.bricklog.feature.set.presentation.dashboard.domain.FeaturedTheme
import hu.piware.bricklog.feature.set.presentation.dashboard.utils.FEATURED_THEMES
import hu.piware.bricklog.ui.theme.BricklogTheme
import org.jetbrains.compose.resources.imageResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.max

@Composable
fun FeaturedThemesCarousel(
    modifier: Modifier = Modifier,
    onItemClick: (FeaturedTheme) -> Unit,
) {
    val carouselState = rememberCarouselState { FEATURED_THEMES.count() }

    HorizontalMultiBrowseCarousel(
        modifier = modifier,
        state = carouselState,
        preferredItemWidth = 200.dp,
        itemSpacing = 12.dp,
        flingBehavior = CarouselDefaults.multiBrowseFlingBehavior(state = carouselState)
    ) { i ->
        val item = FEATURED_THEMES[i]

        Box(
            contentAlignment = Alignment.BottomStart
        ) {
            Image(
                bitmap = imageResource(item.imageResId),
                modifier = Modifier
                    .height(286.dp)
                    .fillMaxWidth()
                    .maskClip(MaterialTheme.shapes.extraLarge)
                    .background(color = item.color)
                    .clickable { onItemClick(item) },
                contentDescription = stringResource(item.contentDescriptionResId),
                contentScale = ContentScale.Crop,
                alignment = Alignment.TopCenter
            )

            Text(
                modifier = Modifier
                    .padding(8.dp)
                    .graphicsLayer {
                        alpha = lerp(
                            0f,
                            1f,
                            max(
                                size.width - (carouselItemDrawInfo.maxSize) +
                                        carouselItemDrawInfo.size,
                                0f
                            ) / size.width
                        )
                        translationX = carouselItemDrawInfo.maskRect.left + 8.dp.toPx()

                    },
                text = stringResource(item.contentDescriptionResId),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }
    }
}

@Preview
@Composable
private fun FeaturedThemesCarouselPreview() {
    BricklogTheme {
        FeaturedThemesCarousel(
            onItemClick = {}
        )
    }
}

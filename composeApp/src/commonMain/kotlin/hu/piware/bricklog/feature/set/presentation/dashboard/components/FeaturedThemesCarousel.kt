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
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.theme_architecture
import bricklog.composeapp.generated.resources.theme_city
import bricklog.composeapp.generated.resources.theme_creator
import bricklog.composeapp.generated.resources.theme_disney
import bricklog.composeapp.generated.resources.theme_harry_potter
import bricklog.composeapp.generated.resources.theme_icons
import bricklog.composeapp.generated.resources.theme_ideas
import bricklog.composeapp.generated.resources.theme_minecraft
import bricklog.composeapp.generated.resources.theme_name_architecture
import bricklog.composeapp.generated.resources.theme_name_city
import bricklog.composeapp.generated.resources.theme_name_creator
import bricklog.composeapp.generated.resources.theme_name_disney
import bricklog.composeapp.generated.resources.theme_name_harry_potter
import bricklog.composeapp.generated.resources.theme_name_icons
import bricklog.composeapp.generated.resources.theme_name_ideas
import bricklog.composeapp.generated.resources.theme_name_minecraft
import bricklog.composeapp.generated.resources.theme_name_ninjago
import bricklog.composeapp.generated.resources.theme_name_star_wars
import bricklog.composeapp.generated.resources.theme_name_super_mario
import bricklog.composeapp.generated.resources.theme_name_technic
import bricklog.composeapp.generated.resources.theme_ninjago
import bricklog.composeapp.generated.resources.theme_star_wars
import bricklog.composeapp.generated.resources.theme_super_mario
import bricklog.composeapp.generated.resources.theme_technic
import hu.piware.bricklog.feature.set.presentation.dashboard.domain.ThemeCarouselItem
import org.jetbrains.compose.resources.imageResource
import org.jetbrains.compose.resources.stringResource
import kotlin.math.max

val FEATURED_THEMES = listOf(
    ThemeCarouselItem(
        imageResId = Res.drawable.theme_architecture,
        contentDescriptionResId = Res.string.theme_name_architecture,
        color = Color(0xFF2D2D2D),
        theme = "Architecture"
    ),
    ThemeCarouselItem(
        imageResId = Res.drawable.theme_harry_potter,
        contentDescriptionResId = Res.string.theme_name_harry_potter,
        color = Color(0xFF002539),
        theme = "Harry Potter"
    ),
    ThemeCarouselItem(
        imageResId = Res.drawable.theme_icons,
        contentDescriptionResId = Res.string.theme_name_icons,
        color = Color(0xFF242424),
        theme = "Icons"
    ),
    ThemeCarouselItem(
        imageResId = Res.drawable.theme_ideas,
        contentDescriptionResId = Res.string.theme_name_ideas,
        color = Color(0xFF426B38),
        theme = "Ideas"
    ),
    ThemeCarouselItem(
        imageResId = Res.drawable.theme_city,
        contentDescriptionResId = Res.string.theme_name_city,
        color = Color(0xFF304A5E),
        theme = "City"
    ),
    ThemeCarouselItem(
        imageResId = Res.drawable.theme_star_wars,
        contentDescriptionResId = Res.string.theme_name_star_wars,
        color = Color(0xFF121212),
        theme = "Star Wars"
    ),
    ThemeCarouselItem(
        imageResId = Res.drawable.theme_technic,
        contentDescriptionResId = Res.string.theme_name_technic,
        color = Color(0xFF6E2525),
        theme = "Technic"
    ),
    ThemeCarouselItem(
        imageResId = Res.drawable.theme_disney,
        contentDescriptionResId = Res.string.theme_name_disney,
        color = Color(0xFF165947),
        theme = "Disney"
    ),
    ThemeCarouselItem(
        imageResId = Res.drawable.theme_creator,
        contentDescriptionResId = Res.string.theme_name_creator,
        color = Color(0xFFDBA191),
        theme = "Creator"
    ),
    ThemeCarouselItem(
        imageResId = Res.drawable.theme_minecraft,
        contentDescriptionResId = Res.string.theme_name_minecraft,
        color = Color(0xFF3E2745),
        theme = "Minecraft"
    ),
    ThemeCarouselItem(
        imageResId = Res.drawable.theme_ninjago,
        contentDescriptionResId = Res.string.theme_name_ninjago,
        color = Color(0XFF7A6000),
        theme = "Ninjago"
    ),
    ThemeCarouselItem(
        imageResId = Res.drawable.theme_super_mario,
        contentDescriptionResId = Res.string.theme_name_super_mario,
        color = Color(0xFF6b0000),
        theme = "Super Mario"
    )
)

@Composable
fun FeaturedThemesCarousel(
    modifier: Modifier = Modifier,
    onItemClick: (ThemeCarouselItem) -> Unit,
) {
    val carouselState = rememberCarouselState { FEATURED_THEMES.count() }

    HorizontalMultiBrowseCarousel(
        modifier = modifier,
        state = carouselState,
        preferredItemWidth = 224.dp,
        itemSpacing = 8.dp,
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
                                size.width - (carouselItemInfo.maxSize) +
                                        carouselItemInfo.size,
                                0f
                            ) / size.width
                        )
                        translationX = carouselItemInfo.maskRect.left + 8.dp.toPx()

                    },
                text = stringResource(item.contentDescriptionResId),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }
    }
}

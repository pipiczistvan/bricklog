package hu.piware.bricklog.feature.set.presentation.set_detail.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import hu.piware.bricklog.feature.set.domain.model.Image
import hu.piware.bricklog.feature.set.presentation.components.ImageSize
import hu.piware.bricklog.feature.set.presentation.components.SetImage

@Composable
fun BlurredImageBackground(
    image: Image,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .weight(0.45f)
                .fillMaxWidth()
                .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
                .drawWithContent {
                    drawContent()
                    drawRect(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Black,
                                Color.Transparent,
                            )
                        ),
                        blendMode = BlendMode.DstIn
                    )
                }
                .blur(15.dp)
        ) {
            SetImage(
                modifier = Modifier
                    .fillMaxSize(),
                image = image,
                size = ImageSize.SMALL,
                contentScale = ContentScale.Crop
            )
        }

        Box(
            modifier = Modifier
                .weight(0.55f)
                .fillMaxWidth()
        )
    }
}

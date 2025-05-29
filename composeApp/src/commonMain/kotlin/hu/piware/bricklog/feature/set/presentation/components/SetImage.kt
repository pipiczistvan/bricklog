package hu.piware.bricklog.feature.set.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.lego_brick_2x3
import bricklog.composeapp.generated.resources.set_cover
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import hu.piware.bricklog.feature.core.presentation.navigation.BackHandler
import hu.piware.bricklog.feature.set.domain.model.Image
import kotlinx.coroutines.launch
import net.engawapg.lib.zoomable.ScrollGesturePropagation
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

enum class ImageSize {
    SMALL, REGULAR
}

@Composable
fun SetImage(
    image: Image,
    zoomable: Boolean = false,
    contentScale: ContentScale = ContentScale.Fit,
    size: ImageSize = ImageSize.REGULAR,
    modifier: Modifier = Modifier,
) {
    val thumbnailPainter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalPlatformContext.current)
            .data(image.thumbnailURL)
            .crossfade(true)
            .build(),
        error = painterResource(Res.drawable.lego_brick_2x3)
    )

    val painter = when (size) {
        ImageSize.SMALL -> thumbnailPainter
        ImageSize.REGULAR -> {
            rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalPlatformContext.current)
                    .data(image.imageURL)
                    .build(),
                placeholder = thumbnailPainter,
                error = thumbnailPainter
            )
        }
    }

    val zoomState = rememberZoomState()
    val scope = rememberCoroutineScope()

    BackHandler(zoomState.scale != 1.0f) {
        scope.launch {
            zoomState.reset()
        }
    }

    Image(
        modifier = modifier
            .let {
                if (zoomable) it.zoomable(
                    zoomState = zoomState,
                    scrollGesturePropagation = ScrollGesturePropagation.NotZoomed
                ) else it
            },
        painter = painter,
        contentDescription = stringResource(Res.string.set_cover),
        contentScale = contentScale
    )
}

package hu.piware.bricklog.feature.set.presentation.set_scanner.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import co.touchlab.kermit.Logger
import coil3.compose.rememberAsyncImagePainter
import hu.piware.bricklog.feature.set.domain.model.SetDetails
import hu.piware.bricklog.feature.set.presentation.set_scanner.util.SetBarcodeDetection
import hu.piware.bricklog.ui.theme.Dimens
import hu.piware.bricklog.ui.theme.Shapes

private val logger = Logger.withTag("SetDetectionBox")

@Composable
fun SetDetectionBox(
    detection: SetBarcodeDetection,
    onClick: (SetDetails) -> Unit,
    modifier: Modifier = Modifier,
) {
    val imagePainter = rememberAsyncImagePainter(detection.set?.set?.image?.thumbnailURL)

    val rawBounds = detection.bounds

    val left = minOf(rawBounds.left, rawBounds.right)
    val top = minOf(rawBounds.top, rawBounds.bottom)
    val right = maxOf(rawBounds.left, rawBounds.right)
    val bottom = maxOf(rawBounds.top, rawBounds.bottom)
    val width = (right - left).coerceAtLeast(1f)
    val height = (bottom - top).coerceAtLeast(1f)

    val (w, h) = with(LocalDensity.current) {
        Pair(width.toDp(), height.toDp())
    }
    val (x, y) = with(LocalDensity.current) {
        Pair(left.toDp(), top.toDp())
    }

    Box(
        modifier
            .offset(x, y)
            .size(w, h)
            .clip(Shapes.large)
            .background(
                color = Color.White,
                shape = Shapes.large,
            )
            .border(
                width = Dimens.ExtraSmallPadding.size,
                color = Color.Black,
                shape = Shapes.large,
            )
            .clickable {
                if (detection.set != null) {
                    onClick(detection.set)
                }
            },
        contentAlignment = Alignment.Center,
    ) {
        Image(
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimens.ExtraSmallPadding.size),
            contentScale = ContentScale.Fit,
            painter = imagePainter,
            contentDescription = null,
        )
    }
}

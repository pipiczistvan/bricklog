package hu.piware.bricklog.feature.set.presentation.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import hu.piware.bricklog.feature.core.presentation.sharedElement
import hu.piware.bricklog.feature.set.domain.model.SetDetails
import hu.piware.bricklog.feature.set.domain.model.setID
import hu.piware.bricklog.feature.set.presentation.components.ImageSize
import hu.piware.bricklog.feature.set.presentation.components.SetImage
import hu.piware.bricklog.feature.set.presentation.set_detail.SetDetailArguments
import hu.piware.bricklog.mock.PreviewData
import hu.piware.bricklog.ui.theme.BricklogTheme
import hu.piware.bricklog.ui.theme.Dimens
import hu.piware.bricklog.ui.theme.Shapes
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SetCardRow(
    sets: List<SetDetails>,
    onSetClick: (SetDetailArguments) -> Unit,
    sharedElementPrefix: String,
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Spacer(modifier = Modifier.width(12.dp))
        }
        if (sets.isNotEmpty()) {
            items(sets) { set ->
                SetCard(
                    modifier = Modifier
                        .testTag("set_card")
                        .sharedElement("$sharedElementPrefix/image/${set.setID}"),
                    setDetails = set,
                    onClick = {
                        onSetClick(
                            SetDetailArguments(
                                set.setID,
                                sharedElementPrefix
                            )
                        )
                    }
                )

            }
        } else {
            repeat(12) {
                item {
                    SetCardPlaceholder()
                }
            }
        }

        item {
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

private val rainbowBrush = Brush.linearGradient(
    listOf(
        Color(0xff00ff00),
        Color(0xff00ffff),
        Color(0xff6699ff),
        Color(0xffcc33ff),
        Color(0xffff3399),
        Color(0xffff9966),
        Color(0xffccff66),
    )
)

@Composable
fun SetCard(
    setDetails: SetDetails,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        shape = Shapes.large,
        modifier = modifier
            .clip(Shapes.large)
            .clickable(onClick = onClick)
            .width(160.dp),
        elevation = CardDefaults.outlinedCardElevation()
    ) {
        Box(
            modifier = Modifier
                .clip(Shapes.large)
                .size(160.dp)
                .background(Color.White)
        ) {
            SetImage(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Dimens.SmallPadding.size),
                size = ImageSize.SMALL,
                image = setDetails.set.image,
                contentScale = ContentScale.Fit
            )
        }
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            text = setDetails.set.name ?: "",
            minLines = 2,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleSmall
        )
    }
}

@Composable
private fun SetCardPlaceholder(
    modifier: Modifier = Modifier,
) {
    Card(
        shape = Shapes.large,
        modifier = modifier
            .clip(Shapes.large)
            .width(160.dp),
        elevation = CardDefaults.outlinedCardElevation()
    ) {
        Box(
            modifier = Modifier
                .clip(Shapes.large)
                .size(160.dp)
                .background(Color.White)
        ) {
        }
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            text = "",
            minLines = 2,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleSmall
        )
    }
}

@Preview
@Composable
private fun SetCardPreview() {
    BricklogTheme {
        SetCard(
            setDetails = PreviewData.sets[0],
            onClick = {}
        )
    }
}

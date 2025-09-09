package hu.piware.bricklog.feature.set.presentation.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import hu.piware.bricklog.feature.set.presentation.dashboard.DashboardViewModel.Companion.FEATURED_SETS_ROW_LIMIT
import hu.piware.bricklog.mock.PreviewData
import hu.piware.bricklog.ui.theme.Dimens
import hu.piware.bricklog.ui.theme.Shapes
import org.jetbrains.compose.ui.tooling.preview.Preview

private val SetCardWidth = 160.dp

@Composable
fun FeaturedSetsRow(
    title: String,
    sets: List<SetDetails>?,
    sharedElementPrefix: String, // TODO: refactor share element prefixes
    onShowMoreClick: () -> Unit,
    onSetClick: (SetDetails) -> Unit,
    modifier: Modifier = Modifier,
) {
    FeaturedRow(
        title = title,
        items = sets,
        onShowMoreClick = onShowMoreClick,
        placeholderLimit = FEATURED_SETS_ROW_LIMIT,
        modifier = modifier,
    ) { set ->
        if (set != null) {
            SetCard(
                modifier = Modifier
                    .testTag("set_card")
                    .sharedElement("$sharedElementPrefix/image/${set.setID}"),
                setDetails = set,
                onClick = { onSetClick(set) },
            )
        } else {
            SetCardPlaceholder()
        }
    }
}

@Composable
private fun SetCard(
    setDetails: SetDetails,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        shape = Shapes.large,
        modifier = modifier
            .clip(Shapes.large)
            .clickable(onClick = onClick)
            .width(SetCardWidth),
        elevation = CardDefaults.outlinedCardElevation(),
    ) {
        Box(
            modifier = Modifier
                .clip(Shapes.large)
                .size(SetCardWidth)
                .background(Color.White),
        ) {
            SetImage(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Dimens.SmallPadding.size),
                size = ImageSize.SMALL,
                image = setDetails.set.image,
                contentScale = ContentScale.Fit,
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
            style = MaterialTheme.typography.titleSmall,
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
            .width(SetCardWidth),
        elevation = CardDefaults.outlinedCardElevation(),
    ) {
        Box(
            modifier = Modifier
                .clip(Shapes.large)
                .size(SetCardWidth)
                .background(Color.White),
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
            style = MaterialTheme.typography.titleSmall,
        )
    }
}

@Preview
@Composable
private fun FeaturedSetsRowPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
        ) {
            FeaturedSetsRow(
                title = "Featured Sets",
                sets = PreviewData.sets,
                sharedElementPrefix = "featured_set_",
                onSetClick = {},
                onShowMoreClick = {},
            )
        }
    }
}

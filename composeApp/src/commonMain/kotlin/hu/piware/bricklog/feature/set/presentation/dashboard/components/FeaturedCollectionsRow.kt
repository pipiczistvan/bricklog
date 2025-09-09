package hu.piware.bricklog.feature.set.presentation.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import hu.piware.bricklog.feature.collection.domain.model.CollectionDetails
import hu.piware.bricklog.feature.collection.domain.model.CollectionSetDetails
import hu.piware.bricklog.feature.collection.domain.model.containerColor
import hu.piware.bricklog.feature.collection.domain.model.textColor
import hu.piware.bricklog.feature.core.presentation.sharedElement
import hu.piware.bricklog.feature.set.domain.model.SetDetails
import hu.piware.bricklog.feature.set.domain.model.setID
import hu.piware.bricklog.feature.set.presentation.components.ImageSize
import hu.piware.bricklog.feature.set.presentation.components.SetImage
import hu.piware.bricklog.ui.theme.Dimens
import hu.piware.bricklog.ui.theme.Shapes
import org.jetbrains.compose.resources.stringResource

private val CollectionCardWidth = 200.dp

@Composable
fun FeaturedCollectionsRow(
    collections: List<CollectionSetDetails>?,
    showRoleAndOwner: Boolean,
    onShowMoreClick: () -> Unit,
    onCollectionClick: (CollectionSetDetails) -> Unit,
    onSetClick: (CollectionDetails, SetDetails) -> Unit,
    modifier: Modifier = Modifier,
) {
    FeaturedRow(
        title = "Collections", // TODO: Localize
        items = collections,
        onShowMoreClick = onShowMoreClick,
        placeholderLimit = 1,
        modifier = modifier,
    ) { collection ->
        if (collection != null) {
            CollectionCard(
                modifier = Modifier
                    .testTag("collection_card"),
                collectionSetDetails = collection,
                showRoleAndOwner = showRoleAndOwner,
                onClick = { onCollectionClick(collection) },
                onSetClick = { onSetClick(collection.collection, it) },
                sharedElementPrefix = collection.collection.collection.id,
            )
        } else {
            CollectionCardPlaceholder(
                showRoleAndOwner = showRoleAndOwner,
            )
        }
    }
}

@Composable
private fun CollectionCard(
    collectionSetDetails: CollectionSetDetails,
    showRoleAndOwner: Boolean,
    onClick: () -> Unit,
    onSetClick: (SetDetails) -> Unit,
    sharedElementPrefix: String,
    modifier: Modifier = Modifier,
) {
    Card(
        shape = Shapes.large,
        modifier = modifier
            .clip(Shapes.large)
            .clickable(onClick = onClick)
            .width(CollectionCardWidth),
        elevation = CardDefaults.outlinedCardElevation(),
    ) {
        if (collectionSetDetails.sets.isNotEmpty()) {
            LazyVerticalGrid(
                modifier = Modifier
                    .size(CollectionCardWidth),
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(Dimens.ExtraSmallPadding.size),
            ) {
                items(collectionSetDetails.sets.take(4)) { setDetails ->
                    Box(
                        modifier = Modifier
                            .sharedElement("$sharedElementPrefix/image/${setDetails.setID}")
                            .padding(Dimens.ExtraSmallPadding.size)
                            .aspectRatio(1f)
                            .clip(Shapes.large)
                            .background(Color.White)
                            .clickable {
                                onSetClick(setDetails)
                            },
                        contentAlignment = Alignment.Center,
                    ) {
                        SetImage(
                            modifier = Modifier
                                .padding(Dimens.SmallPadding.size),
                            size = ImageSize.SMALL,
                            image = setDetails.set.image,
                            contentScale = ContentScale.Fit,
                        )
                    }
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .size(CollectionCardWidth),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    modifier = Modifier.padding(Dimens.MediumPadding.size),
                    text = "No items in this collection.",
                    textAlign = TextAlign.Center,
                )
            }
        }
        Row {
            Icon(
                modifier = Modifier
                    .padding(start = Dimens.SmallPadding.size, top = Dimens.SmallPadding.size)
                    .size(20.dp),
                imageVector = collectionSetDetails.collection.collection.icon.outlinedIcon,
                contentDescription = null,
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.SmallPadding.size),
                text = collectionSetDetails.collection.collection.name,
                minLines = 2,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleSmall,
            )
        }
        if (showRoleAndOwner) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(Dimens.SmallPadding.size),
                horizontalArrangement = Arrangement.End,
            ) {
                Badge(
                    containerColor = collectionSetDetails.collection.role.containerColor,
                    contentColor = collectionSetDetails.collection.role.textColor,
                ) {
                    Text(stringResource(collectionSetDetails.collection.role.stringRes))
                }
            }
        }
    }
}

@Composable
private fun CollectionCardPlaceholder(
    showRoleAndOwner: Boolean,
    modifier: Modifier = Modifier,
) {
    Card(
        shape = Shapes.large,
        modifier = modifier
            .clip(Shapes.large)
            .width(CollectionCardWidth),
        elevation = CardDefaults.outlinedCardElevation(),
    ) {
        LazyVerticalGrid(
            modifier = Modifier
                .size(CollectionCardWidth),
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(Dimens.ExtraSmallPadding.size),
        ) {
            items(4) { setDetails ->
                Box(
                    modifier = Modifier
                        .padding(Dimens.ExtraSmallPadding.size)
                        .aspectRatio(1f)
                        .clip(Shapes.large)
                        .background(Color.White),
                ) {
                }
            }
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
        if (showRoleAndOwner) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(Dimens.SmallPadding.size),
                horizontalArrangement = Arrangement.End,
            ) {
                Badge(
                    containerColor = Color.White,
                    contentColor = Color.Black,
                ) {
                    Text("")
                }
            }
        }
    }
}

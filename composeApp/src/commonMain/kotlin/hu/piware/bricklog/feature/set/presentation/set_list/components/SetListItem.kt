package hu.piware.bricklog.feature.set.presentation.set_list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hu.piware.bricklog.feature.collection.domain.model.CollectionDetails
import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.collection.domain.model.isEditable
import hu.piware.bricklog.feature.set.domain.model.SetDetails
import hu.piware.bricklog.feature.set.domain.model.SetId
import hu.piware.bricklog.feature.set.domain.model.SetPriceDetails
import hu.piware.bricklog.feature.set.domain.model.SetStatus
import hu.piware.bricklog.feature.set.domain.model.containerColor
import hu.piware.bricklog.feature.set.domain.model.isInCollection
import hu.piware.bricklog.feature.set.domain.model.setID
import hu.piware.bricklog.feature.set.domain.model.setNumberWithVariant
import hu.piware.bricklog.feature.set.domain.model.textColor
import hu.piware.bricklog.feature.set.presentation.components.ImageSize
import hu.piware.bricklog.feature.set.presentation.components.SetImage
import hu.piware.bricklog.feature.set.presentation.components.formattedPrice
import hu.piware.bricklog.mock.PreviewData
import hu.piware.bricklog.ui.theme.BricklogTheme
import hu.piware.bricklog.ui.theme.Dimens
import hu.piware.bricklog.ui.theme.Shapes
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SetListItem(
    setDetails: SetDetails,
    setPriceDetails: SetPriceDetails?,
    baseCollection: CollectionDetails?,
    onClick: (SetDetails) -> Unit,
    onCollectionToggle: (SetId, CollectionId) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .clip(Shapes.large)
            .border(3.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f), Shapes.large)
            .clickable(onClick = { onClick(setDetails) }),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            Row(
                modifier =
                    Modifier
                        .padding(12.dp)
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Box(
                    modifier = Modifier
                        .clip(Shapes.medium)
                        .background(Color.White)
                        .height(100.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    SetImage(
                        image = setDetails.set.image,
                        modifier = Modifier
                            .aspectRatio(
                                ratio = 1f,
                                matchHeightConstraintsFirst = true,
                            )
                            .padding(Dimens.SmallPadding.size),
                        size = ImageSize.SMALL,
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f),
                ) {
                    Text(
                        text = setDetails.set.theme ?: "",
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = "# ${setDetails.setNumberWithVariant}",
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = setDetails.set.name ?: "",
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Spacer(
                        modifier = Modifier.weight(1f),
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(Dimens.ExtraSmallPadding.size),
                        verticalArrangement = Arrangement.spacedBy(Dimens.ExtraSmallPadding.size),
                    ) {
                        if (setDetails.status != SetStatus.UNKNOWN) {
                            SetAttributeChip(
                                text = stringResource(setDetails.status.statusRes),
                                size = ChipSize.SMALL,
                                color = setDetails.status.containerColor,
                                textColor = setDetails.status.textColor,
                            )
                        }
                        if (setPriceDetails != null) {
                            SetAttributeChip(
                                text = formattedPrice(setPriceDetails),
                                size = ChipSize.SMALL,
                                color = setDetails.priceCategory.containerColor,
                                textColor = setDetails.priceCategory.textColor,
                            )
                        }
                    }
                }
            }

            if (baseCollection != null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.TopEnd,
                ) {
                    IconButton(
                        onClick = {
                            onCollectionToggle(
                                setDetails.setID,
                                baseCollection.collection.id,
                            )
                        },
                        enabled = baseCollection.isEditable,
                    ) {
                        Icon(
                            imageVector =
                                if (setDetails.isInCollection(baseCollection.collection.id)) {
                                    baseCollection.collection.icon.filledIcon
                                } else {
                                    baseCollection.collection.icon.outlinedIcon
                                },
                            contentDescription = null,
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun SetListItemPreview() {
    BricklogTheme {
        SetListItem(
            setDetails = PreviewData.sets[0],
            setPriceDetails = null,
            baseCollection = null,
            onClick = {},
            onCollectionToggle = { _, _ -> },
        )
    }
}

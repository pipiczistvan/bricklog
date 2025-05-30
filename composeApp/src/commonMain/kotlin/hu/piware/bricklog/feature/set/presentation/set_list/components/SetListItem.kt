package hu.piware.bricklog.feature.set.presentation.set_list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
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
import hu.piware.bricklog.feature.set.domain.model.SetDetails
import hu.piware.bricklog.feature.set.domain.model.SetStatus
import hu.piware.bricklog.feature.set.domain.model.containerColor
import hu.piware.bricklog.feature.set.domain.model.isFavourite
import hu.piware.bricklog.feature.set.domain.model.setID
import hu.piware.bricklog.feature.set.domain.model.textColor
import hu.piware.bricklog.feature.set.presentation.components.ImageSize
import hu.piware.bricklog.feature.set.presentation.components.SetImage
import hu.piware.bricklog.ui.theme.Shapes
import org.jetbrains.compose.resources.stringResource

@Composable
fun SetListItem(
    setDetails: SetDetails,
    onClick: (SetDetails) -> Unit,
    onFavouriteClick: (SetDetails) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .clip(Shapes.large)
            .border(3.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f), Shapes.large)
            .clickable(onClick = { onClick(setDetails) })
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(
                modifier =
                    Modifier
                        .padding(12.dp)
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(Shapes.medium)
                        .background(Color.White)
                        .height(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    SetImage(
                        image = setDetails.set.image,
                        modifier = Modifier
                            .aspectRatio(
                                ratio = 1f,
                                matchHeightConstraintsFirst = true
                            ),
                        size = ImageSize.SMALL
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                ) {
                    Text(
                        text = setDetails.set.theme ?: "",
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = setDetails.set.name ?: "",
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(
                        modifier = Modifier.weight(1f)
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        SetAttributeChip(
                            text = "# ${setDetails.setID}",
                            size = ChipSize.SMALL
                        )
                        if (setDetails.status != SetStatus.UNKNOWN) {
                            SetAttributeChip(
                                text = stringResource(setDetails.status.statusRes),
                                size = ChipSize.SMALL,
                                color = setDetails.status.containerColor,
                                textColor = setDetails.status.textColor
                            )
                        }
                    }
                }
            }


            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.TopEnd
            ) {
                IconButton(
                    onClick = { onFavouriteClick(setDetails) }
                ) {
                    Icon(
                        imageVector = if (setDetails.isFavourite) Icons.Default.Star else Icons.Outlined.StarOutline,
                        contentDescription = null
                    )
                }
            }
        }
    }
}

package hu.piware.bricklog.feature.set.presentation.set_list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import hu.piware.bricklog.feature.set.domain.model.SetStatus
import hu.piware.bricklog.feature.set.domain.model.SetUI
import hu.piware.bricklog.feature.set.domain.model.containerColor
import hu.piware.bricklog.feature.set.domain.model.isFavourite
import hu.piware.bricklog.feature.set.domain.model.setID
import hu.piware.bricklog.feature.set.domain.model.textColor
import hu.piware.bricklog.feature.set.presentation.components.ImageSize
import hu.piware.bricklog.feature.set.presentation.components.SetImage
import hu.piware.bricklog.ui.theme.Dimens
import hu.piware.bricklog.ui.theme.Shapes
import org.jetbrains.compose.resources.stringResource

@Composable
fun SetGridItem(
    setUI: SetUI,
    onClick: (SetUI) -> Unit,
    onFavouriteClick: (SetUI) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .clip(Shapes.large)
            .border(3.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f), Shapes.large)
            .clickable(onClick = { onClick(setUI) })
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier =
                    Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Box(
                    modifier = Modifier
                        .clip(Shapes.medium)
                        .background(Color.White)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    SetImage(
                        image = setUI.set.image,
                        modifier = Modifier
                            .aspectRatio(
                                ratio = 1f,
                                matchHeightConstraintsFirst = true
                            ),
                        size = ImageSize.SMALL
                    )
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(Dimens.ExtraSmallPadding.size)
                ) {
                    Text(
                        text = setUI.set.theme ?: "",
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = setUI.set.name ?: "",
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(Dimens.ExtraSmallPadding.size)
                    ) {
                        SetAttributeChip(
                            text = "# ${setUI.setID}",
                            size = ChipSize.SMALL
                        )
                        if (setUI.status != SetStatus.UNKNOWN) {
                            SetAttributeChip(
                                text = stringResource(setUI.status.statusRes),
                                size = ChipSize.SMALL,
                                color = setUI.status.containerColor,
                                textColor = setUI.status.textColor
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
                    onClick = { onFavouriteClick(setUI) }
                ) {
                    Icon(
                        imageVector = if (setUI.isFavourite) Icons.Default.Star else Icons.Outlined.StarOutline,
                        contentDescription = null
                    )
                }
            }
        }
    }
}

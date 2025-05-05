package hu.piware.bricklog.feature.set.presentation.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import hu.piware.bricklog.feature.core.presentation.sharedElement
import hu.piware.bricklog.feature.set.domain.model.SetUI
import hu.piware.bricklog.feature.set.domain.model.setID
import hu.piware.bricklog.feature.set.presentation.components.AnimatedBorderCard
import hu.piware.bricklog.feature.set.presentation.components.SetImage
import hu.piware.bricklog.feature.set.presentation.set_detail.SetDetailArguments

@Composable
fun SetCardRow(
    sets: List<SetUI>,
    onSetClick: (SetDetailArguments) -> Unit,
    sharedElementPrefix: String,
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Spacer(modifier = Modifier.width(8.dp))
        }
        if (sets.isNotEmpty()) {
            items(sets) { set ->
                SetCard(
                    modifier = Modifier
                        .sharedElement("$sharedElementPrefix/image/${set.setID}"),
                    setUI = set,
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
    setUI: SetUI,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedBorderCard(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        onCardClick = onClick,
        borderWidth = 4.dp,
        gradient = rainbowBrush,
        animationDuration = 5000,
        active = setUI.isFavourite
    ) {
        Box(
            modifier = Modifier
                .width(150.dp)
                .background(Color.White),
            contentAlignment = Alignment.BottomStart
        ) {
            SetImage(
                modifier = Modifier
                    .size(
                        width = 150.dp,
                        height = 200.dp
                    ),
                image = setUI.set.image,
                contentScale = ContentScale.FillHeight
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.55f))
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    text = setUI.set.name ?: "",
                    minLines = 2,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun SetCardPlaceholder(
    modifier: Modifier = Modifier,
) {
    AnimatedBorderCard(
        shape = MaterialTheme.shapes.medium,
        borderWidth = 4.dp,
        active = false
    ) {
        Box(
            modifier = modifier
                .width(150.dp),
            contentAlignment = Alignment.BottomStart
        ) {
            Box(
                modifier = Modifier
                    .size(
                        width = 150.dp,
                        height = 200.dp
                    )
                    .background(Color.White)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.small)
                    .background(Color.Black.copy(alpha = 0.55f))
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    text = "",
                    minLines = 2,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White
                )
            }
        }
    }
}

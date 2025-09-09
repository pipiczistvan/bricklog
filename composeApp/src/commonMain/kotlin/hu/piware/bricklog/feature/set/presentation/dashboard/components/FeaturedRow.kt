package hu.piware.bricklog.feature.set.presentation.dashboard.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.feature_set_dashboard_label_show_more
import hu.piware.bricklog.ui.theme.Dimens
import hu.piware.bricklog.ui.theme.Shapes
import org.jetbrains.compose.resources.stringResource

@Composable
fun <T> FeaturedRow(
    title: String,
    items: List<T>?,
    onShowMoreClick: () -> Unit,
    placeholderLimit: Int = Int.MAX_VALUE,
    modifier: Modifier = Modifier,
    itemContent: @Composable (T?) -> Unit,
) {
    // TODO: Handle empty items
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Dimens.SmallPadding.size),
    ) {
        Title(
            modifier = Modifier
                .padding(start = Dimens.SmallPadding.size),
            title = title,
            onClick = onShowMoreClick,
        )
        HorizontalItemList(
            items = items,
            onShowMoreClick = onShowMoreClick,
            placeholderLimit = placeholderLimit,
            itemContent = itemContent,
        )
    }
}

@Composable
private fun Title(
    title: String,
    onClick: (() -> Unit),
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier
                .clip(Shapes.large)
                .clickable(onClick = onClick)
                .padding(Dimens.SmallPadding.size),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
            )
        }
    }
}

@Composable
private fun <T> HorizontalItemList(
    items: List<T>?,
    onShowMoreClick: () -> Unit,
    placeholderLimit: Int,
    modifier: Modifier = Modifier,
    itemContent: @Composable (T?) -> Unit,
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Spacer(modifier = Modifier.width(4.dp))
        }
        if (items != null) {
            items(items) { item ->
                itemContent(item)
            }
        } else {
            repeat(placeholderLimit) {
                item {
                    itemContent(null)
                }
            }
        }

        item {
            ShowMoreItem(
                onClick = onShowMoreClick,
                itemContent = itemContent,
            )
        }

        item {
            Spacer(modifier = Modifier.width(4.dp))
        }
    }
}

@Composable
private fun <T> ShowMoreItem(
    onClick: () -> Unit,
    itemContent: @Composable (T?) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier.alpha(0f),
            ) {
                itemContent(null)
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(stringResource(Res.string.feature_set_dashboard_label_show_more))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                )
            }
        }
    }
}

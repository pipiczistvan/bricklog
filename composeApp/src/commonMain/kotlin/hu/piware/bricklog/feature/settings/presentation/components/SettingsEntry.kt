package hu.piware.bricklog.feature.settings.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import hu.piware.bricklog.ui.theme.Dimens
import hu.piware.bricklog.ui.theme.Shapes

@Composable
fun SettingsEntryButton(
    modifier: Modifier = Modifier,
    title: String,
    onClick: () -> Unit = { },
    content: @Composable RowScope.() -> Unit,
) {
    SettingsEntry(
        modifier = modifier
            .clip(Shapes.large)
            .background(color = MaterialTheme.colorScheme.surface)
            .clickable { onClick() },
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = Dimens.UpperMediumPadding.size,
                vertical = Dimens.MediumPadding.size,
            ),
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                content()
            }
        }
    }
}

@Composable
fun SettingsEntrySwitch(
    modifier: Modifier = Modifier,
    title: String,
    subTitle: String,
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
) {
    SettingsEntry(
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .padding(
                    horizontal = Dimens.UpperMediumPadding.size,
                    vertical = Dimens.MediumPadding.size,
                )
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Text(
                    text = subTitle,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
            HorizontalDivider(
                modifier = Modifier
                    .padding(
                        horizontal = Dimens.MediumPadding.size,
                        vertical = Dimens.SmallPadding.size,
                    )
                    .width(DividerDefaults.Thickness)
                    .fillMaxHeight(),
            )
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
            )
        }
    }
}

@Composable
fun SettingsEntry(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = modifier
            .fillMaxWidth(),
    ) {
        content()
    }
}

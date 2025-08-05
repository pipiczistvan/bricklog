package hu.piware.bricklog.feature.settings.presentation.appearance.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hu.piware.bricklog.ui.theme.Dimens

@Composable
fun DoubleHorizontalDivider(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(vertical = Dimens.SmallPadding.size),
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        HorizontalDivider()
        HorizontalDivider()
    }
}

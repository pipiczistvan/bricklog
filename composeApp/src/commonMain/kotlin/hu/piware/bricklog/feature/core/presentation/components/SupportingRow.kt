package hu.piware.bricklog.feature.core.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import hu.piware.bricklog.ui.theme.Dimens

@Composable
fun SupportingRow(
    enabled: Boolean = true,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
    content: @Composable RowScope.() -> Unit,
) {
    CompositionLocalProvider {
        val textStyleColor = if (enabled) colors.unfocusedTextColor else colors.disabledTextColor
        ProvideTextStyle(value = MaterialTheme.typography.bodySmall.merge(color = textStyleColor)) {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(Dimens.SmallPadding.size),
            ) {
                content()
            }
        }
    }
}

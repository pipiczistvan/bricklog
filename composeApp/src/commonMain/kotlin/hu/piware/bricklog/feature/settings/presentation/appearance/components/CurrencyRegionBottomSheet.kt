package hu.piware.bricklog.feature.settings.presentation.appearance.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.feature_settings_appearance_currency_region_bottom_sheet_title
import hu.piware.bricklog.feature.core.presentation.components.SingleSelectBottomSheet
import hu.piware.bricklog.feature.currency.domain.model.CurrencyRegion
import hu.piware.bricklog.feature.currency.domain.model.toUiText
import org.jetbrains.compose.resources.stringResource

@Composable
fun CurrencyRegionBottomSheet(
    selectedItem: CurrencyRegion,
    onSelectionChange: (CurrencyRegion) -> Unit,
    onDismiss: () -> Unit,
) {
    SingleSelectBottomSheet(
        modifier = Modifier.testTag("appearance:currency_region_bottom_sheet"),
        title = stringResource(Res.string.feature_settings_appearance_currency_region_bottom_sheet_title),
        selectedItem = selectedItem,
        availableOptions = CurrencyRegion.entries,
        onSelectionChange = onSelectionChange,
        onDismiss = onDismiss,
    ) { option, isSelected ->
        Text(
            text = option.toUiText().asString(),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

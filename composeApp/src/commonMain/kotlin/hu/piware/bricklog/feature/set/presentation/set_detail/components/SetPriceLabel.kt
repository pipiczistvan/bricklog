@file:OptIn(ExperimentalMaterial3Api::class)

package hu.piware.bricklog.feature.set.presentation.set_detail.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.feature_set_detail_price_info_dialog_label
import hu.piware.bricklog.feature.core.presentation.components.AlertIconButton
import hu.piware.bricklog.feature.set.domain.model.SetPriceDetails
import hu.piware.bricklog.feature.set.presentation.components.formattedPrice
import org.jetbrains.compose.resources.stringResource

@Composable
fun SetPriceLabel(
    priceDetails: SetPriceDetails,
) {
    Text(
        text = formattedPrice(priceDetails),
    )
    AlertIconButton {
        Text(stringResource(Res.string.feature_set_detail_price_info_dialog_label))
    }
}

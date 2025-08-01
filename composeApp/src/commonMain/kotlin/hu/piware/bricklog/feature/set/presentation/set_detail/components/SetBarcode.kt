package hu.piware.bricklog.feature.set.presentation.set_detail.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import hu.piware.bricklog.ui.theme.Shapes
import io.github.alexzhirkevich.qrose.oned.BarcodeType
import io.github.alexzhirkevich.qrose.oned.rememberBarcodePainter

@Composable
fun SetBarcode(
    barcode: String,
    format: BarcodeType,
) {
    Column(
        modifier = Modifier
            .clip(Shapes.large)
            .background(Color.White)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        val barCodePainter = rememberBarcodePainter(
            data = barcode,
            type = format,
            brush = SolidColor(Color.Black),
        )
        Image(
            painter = barCodePainter,
            contentDescription = null,
            modifier = Modifier
                .widthIn(max = 250.dp)
                .fillMaxWidth()
                .heightIn(max = 100.dp),
        )

        Text(
            text = barcode,
            style = MaterialTheme.typography.labelMedium,
            color = Color.Black,
        )
    }
}

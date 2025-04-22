package hu.piware.bricklog.feature.set.presentation.dashboard.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.imageResource

@Composable
fun FeatureCard(
    onClick: () -> Unit,
    backgroundColor: Color,
    backgroundImage: DrawableResource,
    title: String,
) {
    Box(
        modifier = Modifier
            .clip(MaterialTheme.shapes.medium)
            .background(color = backgroundColor)
            .size(
                width = 150.dp,
                height = 200.dp
            )
            .clickable { onClick() },
        contentAlignment = Alignment.BottomStart
    ) {
        Image(
            bitmap = imageResource(backgroundImage),
            contentDescription = title,
            contentScale = ContentScale.Crop,
            alignment = Alignment.TopCenter
        )

        Text(
            modifier = Modifier
                .padding(8.dp),
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )
    }
}

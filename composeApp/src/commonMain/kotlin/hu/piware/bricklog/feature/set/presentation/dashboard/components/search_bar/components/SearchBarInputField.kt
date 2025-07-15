@file:OptIn(ExperimentalMaterial3Api::class)

package hu.piware.bricklog.feature.set.presentation.dashboard.components.search_bar.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.testTag
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.feature_set_search_field_hint
import bricklog.composeapp.generated.resources.ic_barcode_scanner
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SearchBarInputField(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onDrawerClick: () -> Unit,
    onScanClick: () -> Unit,
    onClearClick: () -> Unit,
    onSearch: (String) -> Unit,
    query: String,
    onQueryChange: (String) -> Unit,
) {
    val iconAngleProgress: Float by animateFloatAsState(
        targetValue = if (expanded) 0f else 1f,
        animationSpec = tween(
            durationMillis = 200,
            easing = FastOutSlowInEasing
        ),
        label = "iconAngleProgress"
    )

    CompositionLocalProvider(
        LocalTextSelectionColors provides TextSelectionColors(
            handleColor = MaterialTheme.colorScheme.primaryContainer,
            backgroundColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        SearchBarDefaults.InputField(
            modifier = modifier,
            query = query,
            onQueryChange = onQueryChange,
            onSearch = {
                onExpandedChange(false)
                onSearch(it)
            },
            expanded = expanded,
            onExpandedChange = onExpandedChange,
            placeholder = { Text(text = stringResource(Res.string.feature_set_search_field_hint)) },
            leadingIcon = {
                if (!expanded) {
                    IconButton(
                        onClick = onDrawerClick
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = null,
                            modifier = Modifier.rotate((1f - iconAngleProgress) * 270f)
                        )
                    }
                } else {
                    IconButton(
                        modifier = Modifier
                            .testTag("search_bar:back_button"),
                        onClick = {
                            onExpandedChange(false)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            modifier = Modifier.rotate(iconAngleProgress * -270f)
                        )
                    }
                }
            },
            trailingIcon = {
                if (!expanded) {
                    IconButton(
                        modifier = Modifier
                            .testTag("search_bar:scan_button"),
                        onClick = onScanClick
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_barcode_scanner),
                            contentDescription = null,
                            modifier = Modifier.rotate((1f - iconAngleProgress) * -270f)
                        )
                    }
                } else {
                    IconButton(
                        onClick = onClearClick
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = null,
                            modifier = Modifier.rotate(iconAngleProgress * 270f)
                        )
                    }
                }
            }
        )
    }
}

@Preview
@Composable
private fun SearchBarInputFieldPreview() {
    MaterialTheme {
        SearchBarInputField(
            expanded = true,
            onExpandedChange = {},
            onDrawerClick = {},
            onScanClick = {},
            onClearClick = {},
            onSearch = {},
            query = "",
            onQueryChange = {}
        )
    }
}

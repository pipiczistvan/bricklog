@file:OptIn(ExperimentalMaterial3Api::class)

package hu.piware.bricklog.feature.settings.presentation.license

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.feature_settings_license_title
import hu.piware.bricklog.feature.core.presentation.components.ContentColumn
import hu.piware.bricklog.ui.theme.BricklogTheme
import hu.piware.bricklog.ui.theme.OverpassMonoTypography
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LicenseScreenRoot(
    viewModel: LicenseViewModel = koinViewModel(),
    onBackClick: () -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LicenseScreen(
        modifier = Modifier.testTag("license_screen"),
        state = state,
        onAction = { action ->
            when (action) {
                LicenseAction.OnBackClick -> onBackClick()
            }
        },
    )
}

@Composable
private fun LicenseScreen(
    state: LicenseState,
    onAction: (LicenseAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(Res.string.feature_settings_license_title))
                },
                navigationIcon = {
                    IconButton(onClick = { onAction(LicenseAction.OnBackClick) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = null,
                        )
                    }
                },
            )
        },
    ) { padding ->
        CompositionLocalProvider {
            ProvideTextStyle(value = OverpassMonoTypography().bodyMedium) {
                if (state.license != null) {
                    ContentColumn(
                        modifier = Modifier
                            .testTag("license:body")
                            .padding(horizontal = 8.dp)
                            .fillMaxSize(),
                        contentPadding = PaddingValues(
                            top = padding.calculateTopPadding(),
                            bottom = padding.calculateBottomPadding(),
                        ),
                    ) {
                        Text(state.license)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun LicenseScreenPreview() {
    BricklogTheme {
        LicenseScreen(
            state = LicenseState(
                license = "LICENSE",
            ),
            onAction = {},
        )
    }
}

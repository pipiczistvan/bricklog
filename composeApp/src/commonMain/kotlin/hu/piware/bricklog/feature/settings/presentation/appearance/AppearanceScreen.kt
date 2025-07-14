@file:OptIn(ExperimentalMaterial3Api::class)

package hu.piware.bricklog.feature.settings.presentation.appearance

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.appearance_section_theme
import bricklog.composeapp.generated.resources.appearance_title
import hu.piware.bricklog.feature.core.presentation.components.ContentColumn
import hu.piware.bricklog.feature.settings.domain.model.ThemeOption
import hu.piware.bricklog.ui.theme.BricklogTheme
import hu.piware.bricklog.ui.theme.Dimens
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AppearanceScreenRoot(
    viewModel: AppearanceViewModel = koinViewModel(),
    onBackClick: () -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    AppearanceScreen(
        modifier = Modifier.testTag("appearance_screen"),
        state = state,
        onAction = { action ->
            when (action) {
                AppearanceAction.OnBackClick -> onBackClick()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
private fun AppearanceScreen(
    state: AppearanceState,
    onAction: (AppearanceAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(Res.string.appearance_title))
                },
                navigationIcon = {
                    IconButton(onClick = { onAction(AppearanceAction.OnBackClick) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { padding ->
        ContentColumn(
            modifier = Modifier
                .padding(horizontal = Dimens.MediumPadding.size)
                .fillMaxSize(),
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding(),
                bottom = padding.calculateBottomPadding()
            )
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(Dimens.SmallPadding.size)
            ) {
                Text(
                    text = stringResource(Res.string.appearance_section_theme),
                    style = MaterialTheme.typography.titleMedium
                )

                val choices = ThemeOption.entries.toTypedArray()
                SingleChoiceSegmentedButtonRow(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    choices.forEachIndexed { index, themeOption ->
                        SegmentedButton(
                            selected = state.themeOption == themeOption,
                            onClick = {
                                onAction(AppearanceAction.OnThemeOptionChange(themeOption))
                            },
                            shape = SegmentedButtonDefaults.itemShape(
                                index = index,
                                count = choices.count()
                            )
                        ) {
                            Text(stringResource(themeOption.titleRes))
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun AppearanceScreenPreview() {
    BricklogTheme {
        AppearanceScreen(
            state = AppearanceState(),
            onAction = {}
        )
    }
}

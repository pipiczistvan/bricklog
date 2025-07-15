@file:OptIn(ExperimentalMaterial3Api::class)

package hu.piware.bricklog.feature.settings.presentation.changelog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.feature_set_dashboard_changelog_title
import hu.piware.bricklog.feature.core.presentation.components.ContentColumn
import hu.piware.bricklog.feature.settings.presentation.changelog.components.ReleaseItem
import hu.piware.bricklog.mock.PreviewData
import hu.piware.bricklog.ui.theme.BricklogTheme
import hu.piware.bricklog.ui.theme.Dimens
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ChangelogScreenRoot(
    viewModel: ChangelogViewModel = koinViewModel(),
    onBackClick: () -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    ChangelogScreen(
        modifier = Modifier.testTag("changelog_screen"),
        state = state,
        onAction = { action ->
            when (action) {
                ChangelogAction.OnBackClick -> onBackClick()
            }
        }
    )
}

@Composable
private fun ChangelogScreen(
    state: ChangelogState,
    onAction: (ChangelogAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(Res.string.feature_set_dashboard_changelog_title))
                },
                navigationIcon = {
                    IconButton(onClick = { onAction(ChangelogAction.OnBackClick) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { padding ->
        if (state.changelog != null) {
            ContentColumn(
                modifier = Modifier
                    .padding(horizontal = Dimens.MediumPadding.size)
                    .fillMaxSize(),
                contentPadding = PaddingValues(
                    top = padding.calculateTopPadding(),
                    bottom = padding.calculateBottomPadding()
                ),
                verticalArrangement = Arrangement.spacedBy(Dimens.MediumPadding.size)
            ) {
                state.changelog.releases.forEach { release ->
                    ReleaseItem(
                        release = release
                    )
                    if (release != state.changelog.releases.last()) {
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun ChangelogScreenPreview() {
    BricklogTheme {
        ChangelogScreen(
            state = ChangelogState(
                changelog = PreviewData.changelog
            ),
            onAction = {}
        )
    }
}

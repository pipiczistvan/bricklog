@file:OptIn(ExperimentalMaterial3Api::class)

package hu.piware.bricklog.feature.settings.presentation.changelog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.changelog_build_version
import bricklog.composeapp.generated.resources.changelog_title
import hu.piware.bricklog.feature.core.presentation.components.ContentColumn
import hu.piware.bricklog.feature.settings.domain.model.Change
import hu.piware.bricklog.feature.settings.domain.model.ChangeType
import hu.piware.bricklog.feature.settings.domain.model.Release
import hu.piware.bricklog.ui.theme.Dimens
import org.jetbrains.compose.resources.stringResource
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
fun ChangelogScreen(
    state: ChangelogState,
    onAction: (ChangelogAction) -> Unit,
    modifier: Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(Res.string.changelog_title))
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
                )
            ) {
                state.changelog.releases.forEach { release ->
                    ReleaseItem(
                        release = release
                    )
                }
            }
        }
    }
}

@Composable
private fun ReleaseItem(
    release: Release,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = stringResource(
                Res.string.changelog_build_version,
                release.version,
                release.build
            ),
            style = MaterialTheme.typography.titleLarge
        )
        release.changes.forEach { change ->
            ChangeRow(
                modifier = Modifier.padding(start = 8.dp),
                change = change
            )
        }
    }
}

@Composable
private fun ChangeRow(
    change: Change,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = when (change.type) {
                ChangeType.FEATURE -> Icons.Outlined.StarOutline
                ChangeType.BUGFIX -> Icons.Outlined.BugReport
            },
            contentDescription = null
        )
        Text(text = change.description)
    }
}

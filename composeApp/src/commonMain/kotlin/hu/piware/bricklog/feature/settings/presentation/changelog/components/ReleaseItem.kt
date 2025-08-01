package hu.piware.bricklog.feature.settings.presentation.changelog.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.feature_set_dashboard_changelog_title_build_version
import hu.piware.bricklog.feature.settings.domain.model.Change
import hu.piware.bricklog.feature.settings.domain.model.ChangeType
import hu.piware.bricklog.feature.settings.domain.model.Release
import hu.piware.bricklog.mock.PreviewData
import hu.piware.bricklog.ui.theme.BricklogTheme
import hu.piware.bricklog.ui.theme.Dimens
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ReleaseItem(
    release: Release,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Dimens.SmallPadding.size),
    ) {
        Text(
            text = stringResource(
                Res.string.feature_set_dashboard_changelog_title_build_version,
                release.version,
                release.build,
            ),
            style = MaterialTheme.typography.titleLarge,
        )
        release.changes.forEach { change ->
            ChangeRow(
                modifier = Modifier.padding(start = 8.dp),
                change = change,
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
        horizontalArrangement = Arrangement.spacedBy(Dimens.SmallPadding.size),
    ) {
        Icon(
            imageVector = when (change.type) {
                ChangeType.FEATURE -> Icons.Outlined.StarOutline
                ChangeType.BUGFIX -> Icons.Outlined.BugReport
                ChangeType.REFACTOR -> Icons.Outlined.Build
            },
            contentDescription = null,
        )
        Text(text = change.description)
    }
}

@Preview
@Composable
private fun ReleaseItemPreview() {
    BricklogTheme {
        Box(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
        ) {
            ReleaseItem(
                release = PreviewData.releases[0],
            )
        }
    }
}

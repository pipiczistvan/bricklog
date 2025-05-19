package hu.piware.bricklog.feature.settings.presentation.changelog.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.changelog_build_version
import hu.piware.bricklog.feature.settings.domain.model.Change
import hu.piware.bricklog.feature.settings.domain.model.ChangeType
import hu.piware.bricklog.feature.settings.domain.model.Release
import org.jetbrains.compose.resources.stringResource

@Composable
fun ReleaseItem(
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

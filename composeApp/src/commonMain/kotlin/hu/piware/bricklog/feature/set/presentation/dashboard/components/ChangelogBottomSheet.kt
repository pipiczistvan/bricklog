@file:OptIn(ExperimentalMaterial3Api::class)

package hu.piware.bricklog.feature.set.presentation.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.feature_set_dashboard_changelog_bottom_sheet_title
import hu.piware.bricklog.feature.core.presentation.components.BottomSheetHeader
import hu.piware.bricklog.feature.settings.domain.model.Changelog
import hu.piware.bricklog.feature.settings.presentation.changelog.components.ReleaseItem
import hu.piware.bricklog.mock.PreviewData
import hu.piware.bricklog.ui.theme.BricklogTheme
import hu.piware.bricklog.ui.theme.Dimens
import hu.piware.bricklog.ui.theme.OverpassMonoTypography
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ChangelogBottomSheet(
    changelog: Changelog,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        modifier = Modifier.testTag("search_bar:status_filter_bottom_sheet"),
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        ChangelogSheetContent(
            changelog = changelog,
            sheetState = sheetState,
            onDismiss = onDismiss
        )
    }
}

@Composable
private fun ChangelogSheetContent(
    changelog: Changelog,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CompositionLocalProvider {
        ProvideTextStyle(value = OverpassMonoTypography().bodyMedium) {
            Column(
                modifier = modifier
            ) {
                BottomSheetHeader(
                    title = stringResource(Res.string.feature_set_dashboard_changelog_bottom_sheet_title),
                    sheetState = sheetState,
                    onDismiss = onDismiss
                )

                LazyColumn(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimens.MediumPadding.size),
                    verticalArrangement = Arrangement.spacedBy(Dimens.MediumPadding.size)
                ) {
                    changelog.releases.forEach { release ->
                        item {
                            ReleaseItem(release = release)
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun ChangelogSheetContentPreview() {
    BricklogTheme {
        ChangelogSheetContent(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            changelog = PreviewData.changelog,
            sheetState = rememberModalBottomSheetState(),
            onDismiss = {}
        )
    }
}

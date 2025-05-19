@file:OptIn(ExperimentalMaterial3Api::class)

package hu.piware.bricklog.feature.set.presentation.dashboard.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import hu.piware.bricklog.feature.core.presentation.components.BottomSheetHeader
import hu.piware.bricklog.feature.settings.domain.model.Changelog
import hu.piware.bricklog.feature.settings.presentation.changelog.components.ReleaseItem
import hu.piware.bricklog.ui.theme.Dimens
import kotlinx.coroutines.launch

@Composable
fun ChangelogBottomSheet(
    changelog: Changelog,
    onShowBottomSheetChanged: (Boolean) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        modifier = Modifier.testTag("search_bar:status_filter_bottom_sheet"),
        onDismissRequest = {
            onShowBottomSheetChanged(false)
        },
        sheetState = sheetState
    ) {
        BottomSheetHeader(
            title = "Changelog",
            onCloseClick = {
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        onShowBottomSheetChanged(false)
                    }
                }
            }
        )

        LazyColumn(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.MediumPadding.size)
        ) {
            changelog.releases.forEach { release ->
                item {
                    ReleaseItem(release = release)
                }
            }
        }
    }
}

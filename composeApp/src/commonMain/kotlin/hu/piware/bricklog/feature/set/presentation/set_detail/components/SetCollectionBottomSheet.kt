@file:OptIn(ExperimentalMaterial3Api::class)

package hu.piware.bricklog.feature.set.presentation.set_detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.feature_set_detail_collections_sheet_title
import hu.piware.bricklog.feature.collection.domain.model.Collection
import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.core.presentation.components.BottomSheetHeader
import hu.piware.bricklog.feature.core.presentation.components.BottomSheetOption
import hu.piware.bricklog.mock.PreviewData
import hu.piware.bricklog.ui.theme.BricklogTheme
import hu.piware.bricklog.ui.theme.Dimens
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SetCollectionBottomSheet(
    availableOptions: List<Collection>,
    selected: Set<CollectionId>,
    onToggleCollection: (CollectionId) -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        modifier = Modifier.testTag("set_details:set_collection_bottom_sheet"),
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        SetCollectionSheetContent(
            availableOptions = availableOptions,
            selected = selected,
            onToggleCollection = onToggleCollection,
            sheetState = sheetState,
            onDismiss = onDismiss
        )
    }
}

@Composable
private fun SetCollectionSheetContent(
    availableOptions: List<Collection>,
    selected: Set<CollectionId>,
    onToggleCollection: (CollectionId) -> Unit,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        BottomSheetHeader(
            title = stringResource(Res.string.feature_set_detail_collections_sheet_title),
            sheetState = sheetState,
            onDismiss = onDismiss
        )

        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.MediumPadding.size)
        ) {
            items(availableOptions) { option ->
                SetCollectionSheetOption(
                    collection = option,
                    selected = selected.contains(option.id),
                    onClick = {
                        onToggleCollection(option.id)
                    }
                )
            }
        }
    }
}

@Composable
private fun SetCollectionSheetOption(
    collection: Collection,
    selected: Boolean,
    onClick: () -> Unit,
) {
    BottomSheetOption(
        selected = selected, onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.SmallPadding.size),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens.SmallPadding.size)
            ) {
                Icon(
                    imageVector = collection.icon.outlinedIcon,
                    contentDescription = null
                )
                Text(
                    text = collection.name,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Icon(
                modifier = Modifier.alpha(if (selected) 1f else 0f),
                imageVector = Icons.Default.Check,
                contentDescription = null
            )
        }
    }
}

@Preview
@Composable
private fun SetCollectionSheetContentPreview() {
    BricklogTheme {
        SetCollectionSheetContent(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            availableOptions = PreviewData.collections,
            selected = emptySet(),
            onToggleCollection = {},
            sheetState = rememberModalBottomSheetState(),
            onDismiss = {}
        )
    }
}

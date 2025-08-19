@file:OptIn(ExperimentalMaterial3Api::class)

package hu.piware.bricklog.feature.set.presentation.set_detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.feature_set_detail_collections_empty
import bricklog.composeapp.generated.resources.feature_set_detail_collections_title
import hu.piware.bricklog.feature.collection.domain.model.CollectionDetails
import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.core.presentation.components.ActionRow
import hu.piware.bricklog.feature.set.domain.model.SetDetails
import hu.piware.bricklog.feature.set.domain.model.isInCollection
import hu.piware.bricklog.mock.PreviewData
import hu.piware.bricklog.ui.theme.BricklogTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SetCollectionsTable(
    modifier: Modifier = Modifier,
    setDetails: SetDetails,
    availableCollections: List<CollectionDetails>,
    onToggleCollection: (CollectionId) -> Unit,
) {
    var showCollectionSheet by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .weight(1f),
                text = stringResource(Res.string.feature_set_detail_collections_title),
                fontWeight = FontWeight.Bold,
            )
            IconButton(
                onClick = {
                    showCollectionSheet = true
                },
            ) {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = null,
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f)),
        ) {
            if (setDetails.collections.isNotEmpty()) {
                setDetails.collections.forEach {
                    ActionRow(
                        title = it.name,
                        onClick = {
                        },
                        startIcon = {
                            Icon(
                                imageVector =
                                    if (setDetails.isInCollection(it.id)) {
                                        it.icon.filledIcon
                                    } else {
                                        it.icon.outlinedIcon
                                    },
                                contentDescription = null,
                            )
                        },
                    )
                    if (it != setDetails.collections.last()) {
                        HorizontalDivider()
                    }
                }
            } else {
                Text(
                    modifier = Modifier
                        .padding(8.dp),
                    text = stringResource(Res.string.feature_set_detail_collections_empty),
                )
            }
        }
    }

    if (showCollectionSheet) {
        SetCollectionBottomSheet(
            availableOptions = availableCollections,
            selectedItems = setDetails.collections.map { it.id },
            onToggleCollection = onToggleCollection,
            onDismiss = { showCollectionSheet = false },
        )
    }
}

@Preview
@Composable
private fun SetCollectionsTablePreview() {
    BricklogTheme {
        Box(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
        ) {
            SetCollectionsTable(
                setDetails = PreviewData.sets.first(),
                availableCollections = PreviewData.collectionDetails,
                onToggleCollection = {},
            )
        }
    }
}

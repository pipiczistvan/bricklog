@file:OptIn(ExperimentalMaterial3Api::class)

package hu.piware.bricklog.feature.set.presentation.set_detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import bricklog.composeapp.generated.resources.set_details_collections
import bricklog.composeapp.generated.resources.set_details_collections_not_found
import hu.piware.bricklog.feature.collection.domain.model.Collection
import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.core.presentation.components.ActionRow
import org.jetbrains.compose.resources.stringResource

@Composable
fun SetCollectionsTable(
    modifier: Modifier = Modifier,
    setCollections: List<Collection>,
    availableCollections: List<Collection>,
    onToggleCollection: (CollectionId) -> Unit,
) {
    var showCollectionSheet by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .weight(1f),
                text = stringResource(Res.string.set_details_collections),
                fontWeight = FontWeight.Bold
            )
            IconButton(
                onClick = {
                    showCollectionSheet = true
                }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = null
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f)),
        ) {
            if (setCollections.isNotEmpty()) {
                setCollections.forEach {
                    ActionRow(
                        title = it.name,
                        onClick = {
                        },
                        startIcon = {
                            Icon(
                                imageVector = it.icon.filledIcon,
                                contentDescription = null
                            )
                        }
                    )
                    if (it != setCollections.last()) {
                        HorizontalDivider()
                    }
                }
            } else {
                Text(
                    modifier = Modifier
                        .padding(8.dp),
                    text = stringResource(Res.string.set_details_collections_not_found)
                )
            }
        }
    }

    if (showCollectionSheet) {
        SetCollectionBottomSheet(
            onShowBottomSheetChanged = { showCollectionSheet = it },
            availableOptions = availableCollections,
            selected = setCollections.map { it.id }.toSet(),
            onToggleCollection = onToggleCollection
        )
    }
}

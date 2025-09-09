package hu.piware.bricklog.feature.set.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import hu.piware.bricklog.feature.collection.domain.model.CollectionDetails
import hu.piware.bricklog.feature.collection.domain.model.containerColor
import hu.piware.bricklog.feature.collection.domain.model.textColor
import hu.piware.bricklog.feature.core.presentation.components.SingleSelectBottomSheet
import hu.piware.bricklog.ui.theme.Dimens
import org.jetbrains.compose.resources.stringResource

@Composable
fun PreferredCollectionBottomSheet(
    availableCollections: List<CollectionDetails>,
    preferredCollection: CollectionDetails,
    showRoleAndOwner: Boolean,
    onPreferredCollectionChange: (CollectionDetails) -> Unit,
    onDismiss: () -> Unit,
) {
    SingleSelectBottomSheet(
        title = "Preferred collection",
        availableOptions = availableCollections,
        selectedItem = preferredCollection,
        onSelectionChange = onPreferredCollectionChange,
        onDismiss = onDismiss,
    ) { details, isSelected ->
        CollectionBottomSheetItem(
            details = details,
            showRoleAndOwner = showRoleAndOwner,
            isSelected = isSelected,
        )
    }
}

@Composable
private fun CollectionBottomSheetItem(
    details: CollectionDetails,
    showRoleAndOwner: Boolean,
    isSelected: Boolean,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier
                    .padding(end = Dimens.SmallPadding.size),
                imageVector = if (isSelected) {
                    details.collection.icon.filledIcon
                } else {
                    details.collection.icon.outlinedIcon
                },
                contentDescription = null,
            )
            Column {
                Text(
                    text = details.collection.name,
                )
                if (showRoleAndOwner) {
                    Text(
                        text = details.collection.owner,
                        style = MaterialTheme.typography.labelSmall,
                    )
                }
            }
        }
        if (showRoleAndOwner) {
            Badge(
                containerColor = details.role.containerColor,
                contentColor = details.role.textColor,
            ) {
                Text(stringResource(details.role.stringRes))
            }
        }
    }
}

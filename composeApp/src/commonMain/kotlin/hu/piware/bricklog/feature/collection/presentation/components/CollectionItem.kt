package hu.piware.bricklog.feature.collection.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import hu.piware.bricklog.feature.collection.domain.model.CollectionDetails
import hu.piware.bricklog.feature.collection.domain.model.CollectionRole
import hu.piware.bricklog.feature.collection.domain.model.containerColor
import hu.piware.bricklog.feature.collection.domain.model.textColor
import hu.piware.bricklog.ui.theme.Dimens
import org.jetbrains.compose.resources.stringResource

@Composable
fun CollectionItem(
    details: CollectionDetails,
    isSelected: Boolean,
    showRoleAndOwner: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
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
                if (showRoleAndOwner && details.role != CollectionRole.OWNER) {
                    Text(
                        text = details.ownerFriendlyName ?: details.collection.owner,
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

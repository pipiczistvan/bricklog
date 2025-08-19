package hu.piware.bricklog.feature.set.presentation.set_list.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Badge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import hu.piware.bricklog.feature.collection.domain.model.CollectionDetails
import hu.piware.bricklog.feature.collection.domain.model.containerColor
import hu.piware.bricklog.feature.collection.domain.model.textColor
import hu.piware.bricklog.ui.theme.Dimens
import org.jetbrains.compose.resources.stringResource

@Composable
fun SetListTitle(
    title: SetListTitle,
) {
    when (title) {
        is SetListTitle.SimpleText -> {
            Text(text = title.value)
        }

        is SetListTitle.CollectionName -> {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens.ExtraSmallPadding.size),
            ) {
                Text(text = title.collection.collection.name)
                if (title.showRole) {
                    Badge(
                        containerColor = title.collection.role.containerColor,
                        contentColor = title.collection.role.textColor,
                    ) {
                        Text(stringResource(title.collection.role.stringRes))
                    }
                }
            }
        }
    }
}

sealed interface SetListTitle {
    data class SimpleText(val value: String) : SetListTitle
    data class CollectionName(
        val collection: CollectionDetails,
        val showRole: Boolean,
    ) : SetListTitle
}

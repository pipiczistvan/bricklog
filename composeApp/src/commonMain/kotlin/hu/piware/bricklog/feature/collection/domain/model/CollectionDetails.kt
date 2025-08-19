package hu.piware.bricklog.feature.collection.domain.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.feature_collection_role_editor
import bricklog.composeapp.generated.resources.feature_collection_role_owner
import bricklog.composeapp.generated.resources.feature_collection_role_viewer
import hu.piware.bricklog.feature.user.domain.model.UserId
import hu.piware.bricklog.ui.theme.BricklogTheme
import org.jetbrains.compose.resources.StringResource

data class CollectionDetails(
    val collection: Collection,
    val role: CollectionRole,
)

enum class CollectionRole(
    val stringRes: StringResource,
) {
    OWNER(Res.string.feature_collection_role_owner),
    EDITOR(Res.string.feature_collection_role_editor),
    VIEWER(Res.string.feature_collection_role_viewer),
}

val CollectionRole.containerColor: Color
    @Composable
    @ReadOnlyComposable
    get() = when (this) {
        CollectionRole.OWNER -> BricklogTheme.colorScheme.active.colorContainer
        CollectionRole.EDITOR -> BricklogTheme.colorScheme.futureVariant.colorContainer
        CollectionRole.VIEWER -> BricklogTheme.colorScheme.retiredVariant.colorContainer
    }

val CollectionRole.textColor: Color
    @Composable
    @ReadOnlyComposable
    get() = when (this) {
        CollectionRole.OWNER -> BricklogTheme.colorScheme.active.onColorContainer
        CollectionRole.EDITOR -> BricklogTheme.colorScheme.futureVariant.onColorContainer
        CollectionRole.VIEWER -> BricklogTheme.colorScheme.retiredVariant.onColorContainer
    }

val CollectionDetails.isEditable: Boolean
    get() = role == CollectionRole.OWNER || role == CollectionRole.EDITOR

fun Collection.toCollectionDetails(userId: UserId): CollectionDetails {
    return CollectionDetails(
        collection = this,
        role = getRole(userId),
    )
}

private fun Collection.getRole(userId: UserId): CollectionRole {
    return when {
        owner == userId -> CollectionRole.OWNER
        shares[userId]?.canWrite == true -> CollectionRole.EDITOR
        else -> CollectionRole.VIEWER
    }
}

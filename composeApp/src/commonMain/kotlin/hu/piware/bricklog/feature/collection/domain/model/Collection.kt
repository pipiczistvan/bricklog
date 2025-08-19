package hu.piware.bricklog.feature.collection.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Sell
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.Sell
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material.icons.outlined.Store
import androidx.compose.ui.graphics.vector.ImageVector
import hu.piware.bricklog.feature.user.domain.model.UserId

typealias CollectionId = String

data class Collection(
    val id: CollectionId = "",
    val owner: UserId,
    val name: String,
    val icon: CollectionIcon,
    val type: CollectionType,
    val shares: Map<UserId, CollectionShare>,
)

data class CollectionShare(
    val canWrite: Boolean,
)

enum class CollectionIcon(
    val filledIcon: ImageVector,
    val outlinedIcon: ImageVector,
) {
    STAR(Icons.Filled.Star, Icons.Outlined.StarOutline),
    HOME(Icons.Filled.Home, Icons.Outlined.Home),
    FAVOURITE(Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder),
    DELETE(Icons.Filled.Delete, Icons.Outlined.Delete),
    STORE(Icons.Filled.Store, Icons.Outlined.Store),
    PAYMENTS(Icons.Filled.Payments, Icons.Outlined.Payments),
    SELL(Icons.Filled.Sell, Icons.Outlined.Sell),
    BOOKMARK(Icons.Filled.Bookmark, Icons.Outlined.BookmarkBorder),
}

val Collection.isNew: Boolean
    get() = id == ""

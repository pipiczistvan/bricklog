package hu.piware.bricklog.feature.set.domain.model

import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.set_sort_launch_date_ascending
import bricklog.composeapp.generated.resources.set_sort_launch_date_descending
import bricklog.composeapp.generated.resources.set_sort_retiring_date_ascending
import bricklog.composeapp.generated.resources.set_sort_retiring_date_descending
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.StringResource

@Serializable
enum class SetSortOption(
    val titleRes: StringResource
) {
    LAUNCH_DATE_ASCENDING(
        titleRes = Res.string.set_sort_launch_date_ascending
    ),
    LAUNCH_DATE_DESCENDING(
        titleRes = Res.string.set_sort_launch_date_descending
    ),
    RETIRING_DATE_ASCENDING(
        titleRes = Res.string.set_sort_retiring_date_ascending
    ),
    RETIRING_DATE_DESCENDING(
        titleRes = Res.string.set_sort_retiring_date_descending
    )
}

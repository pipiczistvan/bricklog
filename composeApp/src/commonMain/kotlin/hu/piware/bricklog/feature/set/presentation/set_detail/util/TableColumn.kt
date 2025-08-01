package hu.piware.bricklog.feature.set.presentation.set_detail.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.EuroSymbol
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.EventBusy
import androidx.compose.material.icons.filled.Height
import androidx.compose.material.icons.filled.OpenInFull
import androidx.compose.material.icons.filled.Tag
import androidx.compose.ui.graphics.vector.ImageVector
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.arrow_range
import bricklog.composeapp.generated.resources.feature_set_detail_table_label_depth
import bricklog.composeapp.generated.resources.feature_set_detail_table_label_exit_date
import bricklog.composeapp.generated.resources.feature_set_detail_table_label_height
import bricklog.composeapp.generated.resources.feature_set_detail_table_label_launch_date
import bricklog.composeapp.generated.resources.feature_set_detail_table_label_minifigures
import bricklog.composeapp.generated.resources.feature_set_detail_table_label_number
import bricklog.composeapp.generated.resources.feature_set_detail_table_label_pieces
import bricklog.composeapp.generated.resources.feature_set_detail_table_label_price_eu
import bricklog.composeapp.generated.resources.feature_set_detail_table_label_price_us
import bricklog.composeapp.generated.resources.feature_set_detail_table_label_weight
import bricklog.composeapp.generated.resources.feature_set_detail_table_label_width
import bricklog.composeapp.generated.resources.feature_set_detail_table_label_year
import bricklog.composeapp.generated.resources.lego_brick_1x1
import bricklog.composeapp.generated.resources.lego_figure_head
import bricklog.composeapp.generated.resources.weight
import hu.piware.bricklog.feature.core.presentation.util.formatDate
import hu.piware.bricklog.feature.set.domain.model.EUPrice
import hu.piware.bricklog.feature.set.domain.model.SetDetails
import hu.piware.bricklog.feature.set.domain.model.localExitDate
import hu.piware.bricklog.feature.set.domain.model.localLaunchDate
import hu.piware.bricklog.feature.set.domain.model.setNumberWithVariant
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import kotlin.math.round

sealed class TableColumn(
    val titleRes: StringResource,
    val data: String?,
) {
    class ImageVectorTableColumn(
        titleRes: StringResource,
        data: String?,
        val icon: ImageVector,
    ) : TableColumn(titleRes, data)

    class DrawableTableColumn(
        titleRes: StringResource,
        data: String?,
        val drawableRes: DrawableResource,
    ) : TableColumn(titleRes, data)
}

fun createFirstSetDetailTableColumns(setDetails: SetDetails): List<TableColumn> {
    return listOf(
        TableColumn.ImageVectorTableColumn(
            titleRes = Res.string.feature_set_detail_table_label_number,
            data = setDetails.setNumberWithVariant,
            icon = Icons.Default.Tag,
        ),
        TableColumn.ImageVectorTableColumn(
            titleRes = Res.string.feature_set_detail_table_label_year,
            data = setDetails.set.year?.toString(),
            icon = Icons.Default.CalendarToday,
        ),
        TableColumn.DrawableTableColumn(
            titleRes = Res.string.feature_set_detail_table_label_pieces,
            data = setDetails.set.pieces?.toString(),
            drawableRes = Res.drawable.lego_brick_1x1,
        ),
        TableColumn.DrawableTableColumn(
            titleRes = Res.string.feature_set_detail_table_label_minifigures,
            data = setDetails.set.minifigs?.toString(),
            drawableRes = Res.drawable.lego_figure_head,
        ),
    )
}

fun createSecondSetDetailTableColumns(setDetails: SetDetails): List<TableColumn> {
    return listOf(
        TableColumn.DrawableTableColumn(
            titleRes = Res.string.feature_set_detail_table_label_width,
            data = setDetails.set.width?.let { "${round(it * 10) / 10.0} cm" },
            drawableRes = Res.drawable.arrow_range,
        ),
        TableColumn.ImageVectorTableColumn(
            titleRes = Res.string.feature_set_detail_table_label_height,
            data = setDetails.set.height?.let { "${round(it * 10) / 10.0} cm" },
            icon = Icons.Default.Height,
        ),
        TableColumn.ImageVectorTableColumn(
            titleRes = Res.string.feature_set_detail_table_label_depth,
            data = setDetails.set.depth?.let { "${round(it * 10) / 10.0} cm" },
            icon = Icons.Default.OpenInFull,
        ),
        TableColumn.DrawableTableColumn(
            titleRes = Res.string.feature_set_detail_table_label_weight,
            data = setDetails.set.weight?.let { "${round(it * 10) / 10.0} kg" },
            drawableRes = Res.drawable.weight,
        ),
    )
}

fun createThirdSetDetailTableColumns(setDetails: SetDetails): List<TableColumn> {
    return listOf(
        TableColumn.ImageVectorTableColumn(
            titleRes = Res.string.feature_set_detail_table_label_launch_date,
            data = setDetails.localLaunchDate?.let { formatDate(it) },
            icon = Icons.Default.EventAvailable,
        ),
        TableColumn.ImageVectorTableColumn(
            titleRes = Res.string.feature_set_detail_table_label_exit_date,
            data = setDetails.localExitDate?.let { formatDate(it) },
            icon = Icons.Default.EventBusy,
        ),
        TableColumn.ImageVectorTableColumn(
            titleRes = Res.string.feature_set_detail_table_label_price_eu,
            data = setDetails.EUPrice?.let { "€ ${round(it * 100) / 100}" },
            icon = Icons.Default.EuroSymbol,
        ),
        TableColumn.ImageVectorTableColumn(
            titleRes = Res.string.feature_set_detail_table_label_price_us,
            data = setDetails.set.USPrice?.let { "$ ${round(it * 100) / 100}" },
            icon = Icons.Default.AttachMoney,
        ),
    )
}

package hu.piware.bricklog.feature.set.presentation.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import hu.piware.bricklog.feature.set.domain.model.SetDetails
import hu.piware.bricklog.feature.set.domain.model.SetFilter
import hu.piware.bricklog.feature.set.presentation.dashboard.DashboardAction
import hu.piware.bricklog.feature.set.presentation.set_list.SetListArguments
import hu.piware.bricklog.mock.PreviewData
import hu.piware.bricklog.ui.theme.Dimens
import hu.piware.bricklog.ui.theme.Shapes
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun FeaturedSetsRow(
    title: String,
    onDashboardAction: (DashboardAction) -> Unit,
    sets: List<SetDetails>,
    filterOverrides: SetFilter,
    sharedElementPrefix: String,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Dimens.SmallPadding.size)
    ) {
        SectionTitle(
            modifier = Modifier.padding(start = Dimens.MediumPadding.size),
            title = title,
            onClick = {
                onDashboardAction(
                    DashboardAction.OnSearchSets(
                        SetListArguments(
                            filterOverrides = filterOverrides,
                            title = title
                        )
                    )
                )
            }
        )
        SetCardRow(
            sets = sets,
            onSetClick = {
                onDashboardAction(
                    DashboardAction.OnSetClick(it)
                )
            },
            sharedElementPrefix = sharedElementPrefix
        )
    }
}

@Composable
private fun SectionTitle(
    title: String,
    onClick: (() -> Unit),
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .clip(Shapes.large)
                .clickable(onClick = onClick)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}

@Preview
@Composable
private fun FeaturedSetsRowPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            FeaturedSetsRow(
                title = "Featured Sets",
                onDashboardAction = {},
                sets = PreviewData.sets,
                filterOverrides = SetFilter(),
                sharedElementPrefix = "featured_set_"
            )
        }
    }
}

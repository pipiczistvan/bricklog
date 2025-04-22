@file:OptIn(ExperimentalMaterial3Api::class)

package hu.piware.bricklog.feature.set.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.last_updated
import bricklog.composeapp.generated.resources.pull_to_refresh_complete_label
import bricklog.composeapp.generated.resources.pull_to_refresh_pull_label
import bricklog.composeapp.generated.resources.pull_to_refresh_refreshing_label
import bricklog.composeapp.generated.resources.pull_to_refresh_release_label
import hu.piware.bricklog.feature.core.presentation.util.formatDateTime
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

private const val maxHeight = 100

@Composable
fun PullToRefreshColumn(
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    lastUpdated: Instant?,
    modifier: Modifier = Modifier,
    state: PullToRefreshState = rememberPullToRefreshState(),
    content: @Composable ColumnScope.() -> Unit
) {
    val indicatorState by remember(isRefreshing, state.distanceFraction) {
        mutableStateOf(
            when {
                isRefreshing -> RefreshIndicatorState.Refreshing
                state.distanceFraction >= 1f -> RefreshIndicatorState.ReachedThreshold
                state.distanceFraction > 0f -> RefreshIndicatorState.PullingDown
                else -> RefreshIndicatorState.Default
            }
        )
    }

    Column(
        modifier = modifier
            .pullToRefresh(
                state = state,
                isRefreshing = isRefreshing,
                onRefresh = onRefresh
            ),
    ) {
        PullToRefreshIndicator(
            indicatorState = indicatorState,
            pullToRefreshProgress = state.distanceFraction,
            lastUpdated = lastUpdated,
        )
        content()
    }
}

@Composable
private fun PullToRefreshIndicator(
    modifier: Modifier = Modifier,
    indicatorState: RefreshIndicatorState,
    pullToRefreshProgress: Float,
    lastUpdated: Instant?
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height((pullToRefreshProgress * maxHeight).dp)
            .padding(15.dp),
        contentAlignment = Alignment.BottomStart,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = stringResource(indicatorState.messageRes),
                style = MaterialTheme.typography.labelMedium
            )
            if (indicatorState == RefreshIndicatorState.Refreshing) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    strokeWidth = 2.dp,
                )
            } else if (lastUpdated != null) {
                Text(
                    text = stringResource(
                        Res.string.last_updated,
                        formatDateTime(lastUpdated.toLocalDateTime(TimeZone.currentSystemDefault()))
                    ),
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}

private enum class RefreshIndicatorState(val messageRes: StringResource) {
    Default(Res.string.pull_to_refresh_complete_label),
    PullingDown(Res.string.pull_to_refresh_pull_label),
    ReachedThreshold(Res.string.pull_to_refresh_release_label),
    Refreshing(Res.string.pull_to_refresh_refreshing_label)
}

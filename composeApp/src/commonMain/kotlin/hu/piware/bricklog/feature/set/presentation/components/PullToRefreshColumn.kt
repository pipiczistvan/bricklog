@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)

package hu.piware.bricklog.feature.set.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
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
import bricklog.composeapp.generated.resources.pull_to_refresh_complete_label
import bricklog.composeapp.generated.resources.pull_to_refresh_pull_label
import bricklog.composeapp.generated.resources.pull_to_refresh_refreshing_label
import bricklog.composeapp.generated.resources.pull_to_refresh_release_label
import kotlinx.datetime.Instant
import org.jetbrains.compose.resources.StringResource

private const val maxHeight = 100

@Composable
fun PullToRefreshColumn(
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    lastUpdated: Instant?,
    modifier: Modifier = Modifier,
    state: PullToRefreshState = rememberPullToRefreshState(),
    content: @Composable ColumnScope.() -> Unit,
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
        Box(
            modifier = modifier
                .height((state.distanceFraction * maxHeight).dp)
        )
        content()
    }


    PullToRefreshIndicator(
        indicatorState = indicatorState,
        pullToRefreshProgress = state.distanceFraction,
        lastUpdated = lastUpdated,
    )
}

@Composable
private fun PullToRefreshIndicator(
    modifier: Modifier = Modifier,
    indicatorState: RefreshIndicatorState,
    pullToRefreshProgress: Float,
    lastUpdated: Instant?,
) {
    Box(
        contentAlignment = Alignment.TopCenter,
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(maxHeight.dp)
                .offset(y = (-maxHeight + pullToRefreshProgress * maxHeight).dp),
            contentAlignment = Alignment.Center,
        ) {
            if (indicatorState == RefreshIndicatorState.Refreshing) {
                ContainedLoadingIndicator()
            } else {
                ContainedLoadingIndicator(
                    progress = { pullToRefreshProgress / 2f }
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

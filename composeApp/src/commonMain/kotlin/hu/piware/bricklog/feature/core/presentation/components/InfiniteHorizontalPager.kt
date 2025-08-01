package hu.piware.bricklog.feature.core.presentation.components

import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun <T> InfiniteHorizontalPager(
    items: List<T>,
    modifier: Modifier = Modifier,
    pageContent: @Composable (page: Int) -> Unit,
) {
    if (items.size > 1) {
        val startPage =
            (Int.MAX_VALUE / 2) - ((Int.MAX_VALUE / 2) % items.size) // align to items[0]
        val pagerState = rememberPagerState(initialPage = startPage) { Int.MAX_VALUE }

        HorizontalPager(
            modifier = modifier,
            state = pagerState,
            beyondViewportPageCount = 1,
        ) { page ->
            val itemIndex = page % items.size // Loop back using modulo
            pageContent(itemIndex)
        }
    } else if (items.size == 1) {
        pageContent(0)
    }
}

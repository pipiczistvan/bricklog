package hu.piware.bricklog

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import hu.piware.bricklog.feature.collection.domain.util.defaultCollections
import hu.piware.bricklog.feature.set.domain.model.Image
import hu.piware.bricklog.feature.set.domain.model.Set
import hu.piware.bricklog.feature.set.domain.model.SetStatus
import hu.piware.bricklog.feature.set.domain.model.SetUI
import hu.piware.bricklog.feature.set.presentation.dashboard.DashboardScreen
import hu.piware.bricklog.feature.set.presentation.dashboard.DashboardState
import hu.piware.bricklog.feature.set.presentation.dashboard.components.search_bar.SetSearchBarState
import hu.piware.bricklog.feature.set.presentation.set_detail.SetDetailScreen
import hu.piware.bricklog.feature.set.presentation.set_detail.SetDetailState
import hu.piware.bricklog.feature.set.presentation.set_list.components.SetListItem
import hu.piware.bricklog.feature.settings.presentation.about.AboutScreen
import hu.piware.bricklog.ui.theme.BricklogTheme
import kotlinx.datetime.Instant

private val sets = (1..100).map {
    val set = Set(
        setID = it,
        name = "Set $it",
        theme = "Theme",
        image = Image(
            thumbnailURL = "https://test.com",
            imageURL = "https://test.com"
        ),
        pieces = it * 10,
        barcodeEAN = "1234567890123",
        barcodeUPC = "123456789012",
        number = "$it",
        year = it,
        minifigs = it,
        height = it.toFloat(),
        width = it.toFloat(),
        depth = it.toFloat(),
        weight = it.toFloat(),
        launchDate = null,
        exitDate = null,
        USPrice = it.toDouble(),
        DEPrice = it.toDouble(),
        lastUpdated = Instant.DISTANT_PAST,
    )

    SetUI(
        set = set,
        collections = defaultCollections,
        status = SetStatus.ACTIVE
    )
}

@Preview
@Composable
private fun DashboardScreenPreview() {
    BricklogTheme {
        DashboardScreen(
            state = DashboardState(),
            onAction = { },
            searchBarState = SetSearchBarState(),
            onSearchBarAction = { }
        )
    }
}

@Preview
@Composable
private fun SetListItemPreview() {
    BricklogTheme {
        SetListItem(
            setUI = sets[0],
            onClick = {},
            onFavouriteClick = {}
        )
    }
}

@Preview
@Composable
private fun SetDetailsScreenPreview() {
    BricklogTheme {
        SetDetailScreen(
            state = SetDetailState(
                setUI = sets[0]
            ),
            onAction = {}
        )
    }
}

@Preview
@Composable
private fun AboutScreenPreview() {
    BricklogTheme {
        AboutScreen(
            onAction = {}
        )
    }
}

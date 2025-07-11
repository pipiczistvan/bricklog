package hu.piware.bricklog

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import hu.piware.bricklog.feature.set.presentation.set_detail.SetDetailScreen
import hu.piware.bricklog.feature.set.presentation.set_detail.SetDetailState
import hu.piware.bricklog.feature.set.presentation.set_list.components.SetListItem
import hu.piware.bricklog.feature.settings.presentation.about.AboutScreen
import hu.piware.bricklog.mock.PreviewData
import hu.piware.bricklog.ui.theme.BricklogTheme

@Preview
@Composable
private fun SetListItemPreview() {
    BricklogTheme {
        SetListItem(
            setDetails = PreviewData.sets[0],
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
                setDetails = PreviewData.sets[0]
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

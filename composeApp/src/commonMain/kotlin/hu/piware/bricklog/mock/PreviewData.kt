package hu.piware.bricklog.mock

import hu.piware.bricklog.feature.collection.domain.util.defaultCollections
import hu.piware.bricklog.feature.set.domain.model.Image
import hu.piware.bricklog.feature.set.domain.model.Set
import hu.piware.bricklog.feature.set.domain.model.SetDetails
import hu.piware.bricklog.feature.set.domain.model.SetStatus
import hu.piware.bricklog.feature.user.domain.model.User
import kotlinx.datetime.Instant

object PreviewData {

    val user = User(
        uid = "mock",
        displayName = "Mock"
    )

    val sets = (1..100).map {
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

        SetDetails(
            set = set,
            collections = defaultCollections,
            status = SetStatus.ACTIVE
        )
    }
}

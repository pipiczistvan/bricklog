package hu.piware.bricklog.mock

import hu.piware.bricklog.feature.collection.domain.model.Collection
import hu.piware.bricklog.feature.collection.domain.model.CollectionIcon
import hu.piware.bricklog.feature.collection.domain.util.defaultCollections
import hu.piware.bricklog.feature.set.domain.model.Image
import hu.piware.bricklog.feature.set.domain.model.Set
import hu.piware.bricklog.feature.set.domain.model.SetDetails
import hu.piware.bricklog.feature.set.domain.model.SetStatus
import hu.piware.bricklog.feature.settings.domain.model.Change
import hu.piware.bricklog.feature.settings.domain.model.ChangeType
import hu.piware.bricklog.feature.settings.domain.model.Changelog
import hu.piware.bricklog.feature.settings.domain.model.Release
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

    val changes = (1..20).map {
        Change(
            description = "Change $it",
            type = ChangeType.entries[it % ChangeType.entries.size]
        )
    }

    val releases = (1..10).map {
        Release(
            version = "v$it",
            build = it,
            changes = changes
        )
    }

    val changelog = Changelog(
        releases = releases
    )

    val collections = (1..10).map {
        Collection(
            id = it,
            name = "Collection $it",
            icon = CollectionIcon.entries[it % CollectionIcon.entries.size]
        )
    }

    val packagingTypes = (1..10).map {
        "Packaging Type $it"
    }

    val themes = (1..10).map {
        "Theme $it"
    }
}

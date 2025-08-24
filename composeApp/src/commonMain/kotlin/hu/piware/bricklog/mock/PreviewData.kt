package hu.piware.bricklog.mock

import hu.piware.bricklog.feature.collection.domain.model.Collection
import hu.piware.bricklog.feature.collection.domain.model.CollectionIcon
import hu.piware.bricklog.feature.collection.domain.model.CollectionType
import hu.piware.bricklog.feature.collection.domain.model.toCollectionDetails
import hu.piware.bricklog.feature.currency.domain.model.CurrencyRate
import hu.piware.bricklog.feature.currency.domain.util.CURRENCY_CODE_EUR
import hu.piware.bricklog.feature.set.domain.model.Image
import hu.piware.bricklog.feature.set.domain.model.Set
import hu.piware.bricklog.feature.set.domain.model.SetDetails
import hu.piware.bricklog.feature.set.domain.model.SetPriceCategory
import hu.piware.bricklog.feature.set.domain.model.SetStatus
import hu.piware.bricklog.feature.settings.domain.model.Change
import hu.piware.bricklog.feature.settings.domain.model.ChangeType
import hu.piware.bricklog.feature.settings.domain.model.Changelog
import hu.piware.bricklog.feature.settings.domain.model.Release
import hu.piware.bricklog.feature.user.domain.manager.SessionManager.Companion.USER_ID_GUEST
import hu.piware.bricklog.feature.user.domain.model.User
import kotlinx.datetime.Instant

object PreviewData {

    val user = User(
        uid = "mock",
        displayName = "Mock",
    )

    val sets = (1..100).map {
        val set = Set(
            setID = it,
            name = "Set $it",
            theme = "Theme",
            image = Image(
                thumbnailURL = "https://test.com",
                imageURL = "https://test.com",
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
            collections = emptyList(),
            status = SetStatus.ACTIVE,
            priceCategory = SetPriceCategory.UNKNOWN,
        )
    }

    val changes = (1..20).map {
        Change(
            description = "Change $it",
            type = ChangeType.entries[it % ChangeType.entries.size],
        )
    }

    val releases = (1..10).map {
        Release(
            version = "v$it",
            build = it,
            changes = changes,
        )
    }

    val changelog = Changelog(
        releases = releases,
    )

    val collections = (1..10).map {
        Collection(
            id = "$it",
            owner = USER_ID_GUEST,
            name = "Collection $it",
            icon = CollectionIcon.entries[it % CollectionIcon.entries.size],
            type = CollectionType.USER_DEFINED,
            shares = emptyMap(),
        )
    }

    val collectionDetails = collections.map {
        it.toCollectionDetails(USER_ID_GUEST)
    }

    val packagingTypes = (1..10).map {
        "Packaging Type $it"
    }

    val themes = (1..10).map {
        "Theme $it"
    }

    val currencyRates = listOf(
        CurrencyRate(
            currencyCode = CURRENCY_CODE_EUR,
            rate = 1.0,
        ),
    )
}

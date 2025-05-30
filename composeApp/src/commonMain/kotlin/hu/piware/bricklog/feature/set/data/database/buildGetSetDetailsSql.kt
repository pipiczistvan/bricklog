package hu.piware.bricklog.feature.set.data.database

import co.touchlab.kermit.Logger
import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.core.data.database.InstantConverter
import hu.piware.bricklog.feature.set.domain.model.DateFilter
import hu.piware.bricklog.feature.set.domain.model.SetQueryOptions
import hu.piware.bricklog.feature.set.domain.model.SetSortOption
import hu.piware.bricklog.feature.set.domain.model.SetStatus
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.days

private val instantConverter = InstantConverter()
private val logger = Logger.withTag("buildGetSetDetailsSql")

fun buildGetSetDetailsSql(queryOptions: SetQueryOptions): String {
    val now = Clock.System.now()

    val querySelect = queryOptions.queries.joinToString(
        separator = " OR "
    ) {
        "name LIKE '%$it%' OR number LIKE '%$it%'"
    }

    val launchDateSelect = buildLaunchDateSelect(queryOptions.launchDate, now)
    val infoCompleteDateSelect = buildInfoCompleteDateSelect(queryOptions.appearanceDate, now)
    val themeSelect = buildThemeSelect(queryOptions.themes)
    val packagingTypeSelect = buildPackagingTypeSelect(queryOptions.packagingTypes)
    val statusSelect = buildStatusSelect(queryOptions.statuses)
    val showIncomplete = buildShowIncompleteSelect(queryOptions.showIncomplete)
    val barcodeSelect = buildBarcodeSelect(queryOptions.barcode)
    val collectionSelect = buildCollectionIdSelect(queryOptions.collectionIds)
    val orderBy = buildOrderBy(queryOptions.sortOption)
    val limit = when (queryOptions.limit) {
        null -> ""
        else -> "LIMIT ${queryOptions.limit}"
    }

    val sql = """
            SELECT * FROM set_details_view 
            WHERE 
                ($querySelect)
                AND ($launchDateSelect)
                AND ($infoCompleteDateSelect)
                AND ($themeSelect)
                AND ($packagingTypeSelect)
                AND ($statusSelect)
                AND ($showIncomplete)
                AND ($barcodeSelect)
                AND ($collectionSelect)
            ORDER BY $orderBy
            $limit
            """.trimMargin()

    logger.d { sql }

    return sql
}

private fun buildLaunchDateSelect(dateFilter: DateFilter, now: Instant): String {
    return buildDateFilterSelect("launchDate", dateFilter, now)
}

private fun buildInfoCompleteDateSelect(dateFilter: DateFilter, now: Instant): String {
    return buildDateFilterSelect("infoCompleteDate", dateFilter, now)
}

private fun buildDateFilterSelect(dateField: String, dateFilter: DateFilter, now: Instant): String {
    val (startDate, endDate) = when (dateFilter) {
        DateFilter.AnyTime -> Pair(null, null)
        DateFilter.OneWeek -> Pair(now - 7.days, now)
        DateFilter.OneMonth -> Pair(now - 30.days, now)
        DateFilter.OneYear -> Pair(now - 365.days, now)
        is DateFilter.Custom -> Pair(
            instantConverter.toInstant(dateFilter.startDate),
            instantConverter.toInstant(dateFilter.endDate)
        )
    }

    val startDateSelect =
        startDate?.let { "$dateField >= ${instantConverter.fromInstant(it)}" } ?: "1"
    val endDateSelect = endDate?.let { "$dateField <= ${instantConverter.fromInstant(it)}" } ?: "1"

    return "$startDateSelect AND $endDateSelect"
}

private fun buildThemeSelect(themes: Set<String>): String {
    return if (themes.isNotEmpty())
        "theme IN (${themes.joinToString(separator = ", ") { "'$it'" }})"
    else
        "1"
}

private fun buildPackagingTypeSelect(packagingTypes: Set<String>): String {
    return if (packagingTypes.isNotEmpty())
        "packagingType IN (${packagingTypes.joinToString(separator = ", ") { "'$it'" }})"
    else
        "1"
}

private fun buildStatusSelect(statuses: Set<SetStatus>): String {
    return if (statuses.isNotEmpty())
        "status IN (${statuses.joinToString(separator = ", ") { "'$it'" }})"
    else
        "1"
}

private fun buildShowIncompleteSelect(showIncomplete: Boolean): String {
    return if (!showIncomplete)
        "infoCompleteDate IS NOT NULL"
    else
        "1"
}

private fun buildBarcodeSelect(barcode: String?): String {
    return if (barcode != null)
        "barcodeEAN = '$barcode' OR barcodeUPC = '$barcode'"
    else
        "1"
}

private fun buildCollectionIdSelect(collectionIds: Set<CollectionId>): String {
    return if (collectionIds.isNotEmpty())
        """EXISTS(
            SELECT * FROM set_collections 
            WHERE
             setId = set_details_view.id
             AND collectionId IN (${collectionIds.joinToString(separator = ", ") { "'$it'" }})
             LIMIT 1
           )""".trimIndent()
    else
        "1"
}

private fun buildOrderBy(sortOption: SetSortOption): String {
    return when (sortOption) {
        SetSortOption.LAUNCH_DATE_ASCENDING -> "COALESCE(launchDate, year) ASC"
        SetSortOption.LAUNCH_DATE_DESCENDING -> "COALESCE(launchDate, year) DESC"
        SetSortOption.RETIRING_DATE_ASCENDING -> "COALESCE(exitDate, year) ASC"
        SetSortOption.RETIRING_DATE_DESCENDING -> "COALESCE(exitDate, year) DESC"
        SetSortOption.APPEARANCE_DATE_DESCENDING -> "infoCompleteDate DESC"
    }
}

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

    val conditions = listOfNotNull(
        buildQuerySelect(queryOptions.queries),
        buildSetIdSelect(queryOptions.setIds),
        buildLaunchDateSelect(queryOptions.launchDate, now),
        buildInfoCompleteDateSelect(queryOptions.appearanceDate, now),
        buildThemeSelect(queryOptions.themes),
        buildPackagingTypeSelect(queryOptions.packagingTypes),
        buildStatusSelect(queryOptions.statuses),
        buildShowIncompleteSelect(queryOptions.showIncomplete),
        buildBarcodeSelect(queryOptions.barcode),
        buildCollectionIdSelect(queryOptions.collectionIds)
    ).joinToString(separator = " AND ") { "($it)" }

    val orderBy = buildOrderBy(queryOptions.sortOption)
    val limit = queryOptions.limit?.let { " LIMIT $it" } ?: ""

    val sql = StringBuilder()
        .append("SELECT * FROM set_details_view")
        .append(if (conditions.isNotEmpty()) " WHERE $conditions" else "")
        .append(" ORDER BY $orderBy")
        .append(limit)
        .toString()

    logger.d { sql }

    return sql
}

private fun buildQuerySelect(queries: List<String>): String? {
    if (queries.isEmpty()) return null

    return queries.joinToString(
        separator = " OR "
    ) {
        "name LIKE '%$it%' OR number LIKE '%$it%'"
    }
}

private fun buildSetIdSelect(setIds: Set<Int>): String? {
    if (setIds.isEmpty()) return null

    return "id IN (${setIds.joinToString(separator = ", ") { "$it" }})"
}

private fun buildLaunchDateSelect(dateFilter: DateFilter, now: Instant): String? {
    return buildDateFilterSelect("launchDate", dateFilter, now)
}

private fun buildInfoCompleteDateSelect(dateFilter: DateFilter, now: Instant): String? {
    return buildDateFilterSelect("infoCompleteDate", dateFilter, now)
}

private fun buildDateFilterSelect(
    dateField: String,
    dateFilter: DateFilter,
    now: Instant,
): String? {
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

    val startDateSelect = startDate?.let { "$dateField >= ${instantConverter.fromInstant(it)}" }
    val endDateSelect = endDate?.let { "$dateField <= ${instantConverter.fromInstant(it)}" }

    return when {
        startDateSelect != null && endDateSelect != null -> "$startDateSelect AND $endDateSelect"
        startDateSelect != null -> startDateSelect
        endDateSelect != null -> endDateSelect
        else -> null
    }
}

private fun buildThemeSelect(themes: Set<String>): String? {
    return buildStringCollectionSelect("theme", themes)
}

private fun buildPackagingTypeSelect(packagingTypes: Set<String>): String? {
    return buildStringCollectionSelect("packagingType", packagingTypes)
}

private fun buildStatusSelect(statuses: Set<SetStatus>): String? {
    return buildStringCollectionSelect("status", statuses.map { it.name })
}

private fun buildShowIncompleteSelect(showIncomplete: Boolean): String? {
    return if (!showIncomplete)
        "infoCompleteDate IS NOT NULL"
    else
        null
}

private fun buildBarcodeSelect(barcode: String?): String? {
    return barcode?.let { "barcodeEAN = '$it' OR barcodeUPC = '$it'" }
}

private fun buildCollectionIdSelect(collectionIds: Set<CollectionId>): String? {
    if (collectionIds.isEmpty()) return null

    return StringBuilder()
        .append("EXISTS(")
        .append("SELECT * FROM set_collections WHERE ")
        .append("setId = set_details_view.id ")
        .append("AND collectionId IN (${collectionIds.joinToString(separator = ", ") { "'$it'" }}) ")
        .append("LIMIT 1")
        .append(")")
        .toString()
}

private fun buildOrderBy(sortOption: SetSortOption): String {
    return when (sortOption) {
        SetSortOption.LAUNCH_DATE_ASCENDING -> "COALESCE(launchDate, year) ASC"
        SetSortOption.LAUNCH_DATE_DESCENDING -> "COALESCE(launchDate, year) DESC"
        SetSortOption.RETIRING_DATE_ASCENDING -> "COALESCE(exitDate, year) ASC"
        SetSortOption.RETIRING_DATE_DESCENDING -> "COALESCE(exitDate, year) DESC"
        SetSortOption.APPEARANCE_DATE_ASCENDING -> "infoCompleteDate ASC"
        SetSortOption.APPEARANCE_DATE_DESCENDING -> "infoCompleteDate DESC"
    }
}

private fun buildStringCollectionSelect(
    columnName: String,
    collection: Collection<String>,
): String? {
    if (collection.isEmpty()) return null

    return "$columnName IN (${collection.joinToString(separator = ", ") { "'${it.sqlEscape()}'" }})"
}

private fun String.sqlEscape() = replace("'", "''")

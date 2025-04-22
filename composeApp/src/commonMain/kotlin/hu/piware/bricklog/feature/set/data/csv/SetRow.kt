package hu.piware.bricklog.feature.set.data.csv

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class SetRow(
    val setID: Int,
    val number: String? = null,
    val numberVariant: Int? = null,
    val name: String? = null,
    val year: Int? = null,
    val theme: String? = null,
    val themeGroup: String? = null,
    val subTheme: String? = null,
    val category: String? = null,
    val released: Boolean? = null,
    val pieces: Int? = null,
    val minifigs: Int? = null,
    val thumbnailURL: String? = null,
    val imageURL: String? = null,
    val bricksetURL: String? = null,
    val USPrice: Double? = null,
    val UKPrice: Double? = null,
    val CAPrice: Double? = null,
    val DEPrice: Double? = null,
    val rating: Float? = null,
    val launchDate: Instant? = null,
    val exitDate: Instant? = null,
    val packagingType: String? = null,
    val availability: String? = null,
    val ageMin: Int? = null,
    val ageMax: Int? = null,
    val height: Float? = null,
    val width: Float? = null,
    val depth: Float? = null,
    val weight: Float? = null,
    val barcodeEAN: String? = null,
    val barcodeUPC: String? = null,
    val lastUpdated: Instant
)

package hu.piware.bricklog.feature.set.domain.model

import kotlinx.datetime.Instant

data class Set(
    val setID: Int,
    val number: String? = null,
    val numberVariant: Int? = null,
    val name: String? = null,
    val year: Int? = null,
    val theme: String? = null,
    val themeGroup: String? = null,
    val subTheme: String? = null,
    val category: String? = null,
    val pieces: Int? = null,
    val minifigs: Int? = null,
    val image: Image = Image(),
    val bricksetURL: String? = null,
    val USPrice: Double? = null,
    val DEPrice: Double? = null,
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
    val lastUpdated: Instant,
    val infoCompleteDate: Instant? = null,
)

data class Image(
    val thumbnailURL: String? = null,
    val imageURL: String? = null,
)

data class Instruction(
    val URL: String? = null,
    val description: String? = null,
)

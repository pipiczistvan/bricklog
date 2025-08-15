package hu.piware.bricklog.feature.set.data.database

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant

@Entity(
    tableName = "sets",
    indices = [
        Index("number"),
        Index("name"),
        Index("theme"),
        Index("USPrice"),
        Index("DEPrice"),
        Index("launchDate"),
        Index("exitDate"),
        Index("packagingType"),
        Index("barcodeEAN"),
        Index("barcodeUPC"),
        Index("infoCompleteDate"),
    ],
)
data class SetEntity(
    @PrimaryKey(autoGenerate = false) val id: Int,
    val number: String?,
    val numberVariant: Int?,
    val name: String?,
    val year: Int?,
    val theme: String?,
    val themeGroup: String?,
    val subTheme: String?,
    val category: String?,
    val pieces: Int?,
    val minifigs: Int?,
    val thumbnailURL: String?,
    val imageURL: String?,
    val bricksetURL: String?,
    val USPrice: Double?,
    val DEPrice: Double?,
    val launchDate: Instant?,
    val exitDate: Instant?,
    val packagingType: String?,
    val availability: String?,
    val ageMin: Int?,
    val ageMax: Int?,
    val height: Float?,
    val width: Float?,
    val depth: Float?,
    val weight: Float?,
    val barcodeEAN: String?,
    val barcodeUPC: String?,
    val lastUpdated: Instant,
    val infoCompleteDate: Instant?,
)

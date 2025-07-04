package hu.piware.bricklog.feature.set.presentation.set_scanner.util

data class CmfBoxDataMatrix(
    val productNumber: String,
    val batchCode: String,
    val internalCode: String,
    val extraCode: String,
)

private val CMF_BOX_DATA_MATRIX_REGEX = Regex("""(\d+) (\d{3}[SR]\d) (\d+) (\d+)""")

fun String.parseCmfBoxDataMatrix(): CmfBoxDataMatrix? {
    val matchResult = CMF_BOX_DATA_MATRIX_REGEX.matchEntire(this.trim())
    return matchResult?.destructured?.let { (product, batch, internal, extra) ->
        CmfBoxDataMatrix(product, batch, internal, extra)
    }
}

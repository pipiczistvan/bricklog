package hu.piware.bricklog.feature.set.data.firebase

import dev.gitlive.firebase.firestore.toMilliseconds
import hu.piware.bricklog.feature.set.domain.model.BatchExportInfo
import hu.piware.bricklog.feature.set.domain.model.CodeList
import hu.piware.bricklog.feature.set.domain.model.Collectible
import hu.piware.bricklog.feature.set.domain.model.ExportBatch
import hu.piware.bricklog.feature.set.domain.model.ExportInfo
import hu.piware.bricklog.feature.set.domain.model.FileUploadResult
import kotlinx.datetime.Instant

fun ExportInfoDocument.toDomainModel(): ExportInfo {
    return ExportInfo(
        id = id!!,
        fileUploads = fileUploads.map { it.toDomainModel() },
        lastUpdated = Instant.fromEpochMilliseconds(lastUpdated!!.toMilliseconds().toLong())
    )
}

fun FileUploadResultDocument.toDomainModel(): FileUploadResult {
    return FileUploadResult(
        serviceId = serviceId!!,
        url = url!!,
        fileId = fileId!!,
        priority = priority!!
    )
}

fun BatchExportInfoDocument.toDomainModel(): BatchExportInfo {
    return BatchExportInfo(
        batches = batches.map { it.toDomainModel() }
    )
}

fun ExportBatchDocument.toDomainModel(): ExportBatch {
    return ExportBatch(
        validFrom = Instant.fromEpochMilliseconds(validFrom!!.toMilliseconds().toLong()),
        validTo = Instant.fromEpochMilliseconds(validTo!!.toMilliseconds().toLong()),
        rowCount = rowCount,
        fileUploads = fileUploads.map { it.toDomainModel() },
    )
}

fun CodeListDocument.toDomainModel(): CodeList {
    return CodeList(
        r = r,
        s = s
    )
}

fun CollectibleDocument.toDomainModel(id: String): Collectible {
    return Collectible(
        setNumber = id,
        setId = setId,
        name = name,
        codes = codes.mapValues { it.value.toDomainModel() }
    )
}

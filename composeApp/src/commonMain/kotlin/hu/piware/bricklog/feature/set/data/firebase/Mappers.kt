package hu.piware.bricklog.feature.set.data.firebase

import dev.gitlive.firebase.firestore.toMilliseconds
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
        id = id!!,
        url = url!!,
        fileId = fileId!!,
        priority = priority!!
    )
}

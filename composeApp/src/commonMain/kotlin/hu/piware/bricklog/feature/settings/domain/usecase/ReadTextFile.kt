@file:OptIn(ExperimentalResourceApi::class)

package hu.piware.bricklog.feature.settings.domain.usecase

import bricklog.composeapp.generated.resources.Res
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.koin.core.annotation.Single

@Single
class ReadTextFile {

    suspend operator fun invoke(path: String): Result<String, DataError.Local> =
        withContext(Dispatchers.IO) {
            try {
                val license = Res.readBytes(path).decodeToString()
                Result.Success(license)
            } catch (e: Exception) {
                Result.Error(DataError.Local.UNKNOWN)
            }
        }
}

package hu.piware.bricklog.feature.set.data.network

import hu.piware.bricklog.BuildKonfig
import hu.piware.bricklog.di.BricksetHttpClient
import hu.piware.bricklog.feature.core.data.network.decodeBody
import hu.piware.bricklog.feature.core.data.network.safeCall
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.core.domain.map
import hu.piware.bricklog.feature.core.domain.onError
import hu.piware.bricklog.feature.core.domain.onSuccess
import hu.piware.bricklog.feature.set.domain.datasource.RemoteSetImageDataSource
import hu.piware.bricklog.feature.set.domain.model.Image
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import org.koin.core.annotation.Single

private const val BASE_URL = "https://brickset.com/api/v3.asmx/getAdditionalImages"

@Single
class KtorRemoteSetImageDataSource(
    @param:BricksetHttpClient private val httpClient: HttpClient,
) : RemoteSetImageDataSource {

    override suspend fun getAdditionalImages(setId: Int): Result<List<Image>, DataError.Remote> {
        return safeCall<BricksetAdditionalImagesDto>(
            transform = { it.decodeBody() },
        ) {
            httpClient.get(BASE_URL) {
                parameter("apiKey", BuildKonfig.BRICKSET_API_KEY)
                parameter("setID", setId)
            }
        }.onError {
            return it
        }.onSuccess {
            if (it.status != BricksetStatus.success) {
                return Result.Error(DataError.Remote.UNKNOWN)
            }
        }.map { data -> data.additionalImages.map { it.toDomainModel() } }
    }
}

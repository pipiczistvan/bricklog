package hu.piware.bricklog.feature.set.data.network

import hu.piware.bricklog.BuildKonfig
import hu.piware.bricklog.feature.core.data.network.HttpClientFactory
import hu.piware.bricklog.feature.core.data.network.decodeBody
import hu.piware.bricklog.feature.core.data.network.safeCall
import hu.piware.bricklog.feature.core.domain.DataError
import hu.piware.bricklog.feature.core.domain.Result
import hu.piware.bricklog.feature.core.domain.map
import hu.piware.bricklog.feature.core.domain.onError
import hu.piware.bricklog.feature.core.domain.onSuccess
import hu.piware.bricklog.feature.set.domain.datasource.RemoteSetInstructionDataSource
import hu.piware.bricklog.feature.set.domain.model.Instruction
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

private const val BASE_URL = "https://brickset.com/api/v3.asmx/getInstructions"

@Single
class KtorRemoteSetInstructionDataSource(
    @Named(HttpClientFactory.BRICKSET) private val httpClient: HttpClient,
) : RemoteSetInstructionDataSource {

    override suspend fun getInstructions(setId: Int): Result<List<Instruction>, DataError.Remote> {
        return safeCall<BricksetInstructionsDto>(
            transform = { it.decodeBody() }
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
        }.map { data -> data.instructions.map { it.toDomainModel() } }
    }
}

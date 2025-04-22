package hu.piware.bricklog.feature.core.data.network

import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json

suspend inline fun <reified T> HttpResponse.decodeBody() = Json.decodeFromString<T>(bodyAsText())

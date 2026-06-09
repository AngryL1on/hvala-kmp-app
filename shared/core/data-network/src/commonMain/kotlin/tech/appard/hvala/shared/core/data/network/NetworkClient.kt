package tech.appard.hvala.shared.core.data.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import tech.appard.hvala.shared.feature.settings.domain.repository.LocaleRepository

class NetworkClient(
    private val localeRepository: LocaleRepository,
) {
    val httpClient: HttpClient = HttpClient {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                },
            )
        }
        defaultRequest {
            header("Accept-Language", localeRepository.getLanguage().code)
        }
    }
}

package tech.appard.hvala.shared.feature.listings.data.source

import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.ExperimentalResourceApi
import tech.appard.hvala.shared.feature.listings.data.model.FilterDefaultsFileDto
import tech.appard.hvala.shared.feature.listings.data.model.ListingsFileDto
import tech.appard.hvala.shared.feature.listings.data.resources.Res

internal class ListingsJsonDataSource(
    private val json: Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    },
) {
    private var listingsFile: ListingsFileDto? = null
    private var filterDefaultsFile: FilterDefaultsFileDto? = null

    suspend fun listings(): ListingsFileDto =
        listingsFile ?: decode("files/listings.json").let {
            json.decodeFromString<ListingsFileDto>(it).also { dto -> listingsFile = dto }
        }

    suspend fun filterDefaults(): FilterDefaultsFileDto =
        filterDefaultsFile ?: decode("files/filter_defaults.json").let {
            json.decodeFromString<FilterDefaultsFileDto>(it).also { dto -> filterDefaultsFile = dto }
        }

    @OptIn(ExperimentalResourceApi::class)
    private suspend fun decode(path: String): String =
        Res.readBytes(path).decodeToString()
}

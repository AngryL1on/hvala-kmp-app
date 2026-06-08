package tech.appard.hvala.shared.feature.profile.data.source

import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.ExperimentalResourceApi
import tech.appard.hvala.shared.feature.profile.data.model.ProfileOverviewDto
import tech.appard.hvala.shared.feature.profile.data.model.SellersFileDto
import tech.appard.hvala.shared.feature.profile.data.resources.Res

internal class ProfileJsonDataSource(
    private val json: Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    },
) {
    private var sellersFile: SellersFileDto? = null
    private var profileOverviewFile: ProfileOverviewDto? = null

    suspend fun sellers(): SellersFileDto =
        sellersFile ?: decode("files/sellers.json").let {
            json.decodeFromString<SellersFileDto>(it).also { dto -> sellersFile = dto }
        }

    suspend fun profileOverview(): ProfileOverviewDto =
        profileOverviewFile ?: decode("files/profile_overview.json").let {
            json.decodeFromString<ProfileOverviewDto>(it).also { dto -> profileOverviewFile = dto }
        }

    @OptIn(ExperimentalResourceApi::class)
    private suspend fun decode(path: String): String =
        Res.readBytes(path).decodeToString()
}

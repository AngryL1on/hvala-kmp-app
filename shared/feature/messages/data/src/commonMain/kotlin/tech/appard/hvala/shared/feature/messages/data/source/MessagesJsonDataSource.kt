package tech.appard.hvala.shared.feature.messages.data.source

import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.ExperimentalResourceApi
import tech.appard.hvala.shared.feature.messages.data.model.ConversationsFileDto
import tech.appard.hvala.shared.feature.messages.data.resources.Res

internal class MessagesJsonDataSource(
    private val json: Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    },
) {
    private var conversationsFile: ConversationsFileDto? = null

    suspend fun conversations(): ConversationsFileDto =
        conversationsFile ?: decode("files/conversations.json").let {
            json.decodeFromString<ConversationsFileDto>(it).also { dto -> conversationsFile = dto }
        }

    @OptIn(ExperimentalResourceApi::class)
    private suspend fun decode(path: String): String =
        Res.readBytes(path).decodeToString()
}

package tech.appard.hvala.shared.feature.messages.domain.repository

import tech.appard.hvala.shared.feature.messages.domain.model.ChatMessage
import tech.appard.hvala.shared.feature.messages.domain.model.ChatThread
import tech.appard.hvala.shared.feature.messages.domain.model.PickedMedia

interface MessagesRepository {
    suspend fun ensureLoaded()

    fun getThreads(): List<ChatThread>

    fun getMessages(threadId: String): List<ChatMessage>

    suspend fun sendMessage(
        threadId: String,
        text: String,
        attachments: List<PickedMedia> = emptyList(),
    )

    suspend fun openChatForListing(listingId: String): String?
}

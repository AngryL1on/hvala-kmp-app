package tech.appard.hvala.shared.feature.messages.domain

import tech.appard.hvala.shared.feature.messages.domain.model.ChatMessage
import tech.appard.hvala.shared.feature.messages.domain.model.ChatThread
import tech.appard.hvala.shared.feature.messages.domain.model.PickedMedia
import tech.appard.hvala.shared.feature.messages.domain.repository.MessagesRepository

class GetChatThreadsUseCase(
    private val messagesRepository: MessagesRepository,
) {
    suspend operator fun invoke(): List<ChatThread> {
        messagesRepository.ensureLoaded()
        return messagesRepository.getThreads()
    }
}

class GetChatMessagesUseCase(
    private val messagesRepository: MessagesRepository,
) {
    suspend operator fun invoke(threadId: String): List<ChatMessage> {
        messagesRepository.ensureLoaded()
        return messagesRepository.getMessages(threadId)
    }
}

class SendChatMessageUseCase(
    private val messagesRepository: MessagesRepository,
) {
    suspend operator fun invoke(
        threadId: String,
        text: String,
        attachments: List<PickedMedia> = emptyList(),
    ) {
        messagesRepository.ensureLoaded()
        messagesRepository.sendMessage(threadId, text, attachments)
    }
}

class OpenChatForListingUseCase(
    private val messagesRepository: MessagesRepository,
) {
    suspend operator fun invoke(listingId: String): String? {
        messagesRepository.ensureLoaded()
        return messagesRepository.openChatForListing(listingId)
    }
}

class GetChatThreadUseCase(
    private val messagesRepository: MessagesRepository,
) {
    suspend operator fun invoke(threadId: String): ChatThread? {
        messagesRepository.ensureLoaded()
        return messagesRepository.getThreads().find { it.id == threadId }
    }
}

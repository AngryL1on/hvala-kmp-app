package tech.appard.hvala.shared.feature.messages.data.repository

import tech.appard.hvala.shared.feature.listings.domain.repository.ListingsRepository
import tech.appard.hvala.shared.feature.messages.data.mapper.toDomain
import tech.appard.hvala.shared.feature.messages.data.model.ConversationFileDto
import tech.appard.hvala.shared.feature.messages.data.source.MessagesJsonDataSource
import tech.appard.hvala.shared.feature.messages.domain.model.ChatMessage
import tech.appard.hvala.shared.feature.messages.domain.model.ChatThread
import tech.appard.hvala.shared.feature.messages.domain.model.PickedMedia
import tech.appard.hvala.shared.feature.messages.domain.repository.MessagesRepository
import tech.appard.hvala.shared.feature.profile.domain.repository.SellerRepository

private class ConversationState(
    var thread: ChatThread,
    val messages: MutableList<ChatMessage>,
)

internal class JsonMessagesRepository(
    private val dataSource: MessagesJsonDataSource,
    private val listingsRepository: ListingsRepository,
    private val sellerRepository: SellerRepository,
) : MessagesRepository {
    private val conversations = linkedMapOf<String, ConversationState>()

    override suspend fun ensureLoaded() {
        if (conversations.isNotEmpty()) return
        listingsRepository.ensureLoaded()
        sellerRepository.ensureLoaded()
        dataSource.conversations().conversations.forEach { dto ->
            conversations[dto.thread.id] = dto.toState()
        }
        refreshThreadPreviews()
    }

    override fun getThreads(): List<ChatThread> =
        conversations.values.map { it.thread.withPreview(it.messages) }

    override fun getMessages(threadId: String): List<ChatMessage> =
        conversations[threadId]?.messages?.toList().orEmpty()

    override suspend fun sendMessage(
        threadId: String,
        text: String,
        attachments: List<PickedMedia>,
    ) {
        val conversation = conversations[threadId] ?: return
        val trimmed = text.trim()
        if (trimmed.isEmpty() && attachments.isEmpty()) return

        val messageText = buildString {
            if (trimmed.isNotEmpty()) append(trimmed)
            if (attachments.isNotEmpty()) {
                if (isNotEmpty()) append('\n')
                append(
                    attachments.joinToString(separator = "\n") { attachment ->
                        "📎 ${attachment.name}"
                    },
                )
            }
        }

        conversation.messages.add(
            ChatMessage(
                id = "$threadId-msg-${conversation.messages.size}",
                text = messageText,
                isOutgoing = true,
            ),
        )
        conversation.thread = conversation.thread.withPreview(conversation.messages)
    }

    override suspend fun openChatForListing(listingId: String): String? {
        listingsRepository.ensureLoaded()
        sellerRepository.ensureLoaded()

        val listing = listingsRepository.getListingById(listingId) ?: return null
        val threadId = "listing-$listingId"

        if (conversations[threadId] == null) {
            conversations[threadId] = ConversationState(
                thread = ChatThread(
                    id = threadId,
                    participantName = listing.sellerName,
                    lastMessagePreview = "",
                    avatarColorArgb = sellerRepository.getSellerById(listing.sellerId)?.avatarColorArgb
                        ?: avatarColorFor(listing.sellerName),
                    listingId = listing.id,
                    sellerId = listing.sellerId,
                    listingTitle = listing.title,
                    listingPriceUsd = listing.priceUsd,
                    listingPriceRub = listing.priceRub,
                ),
                messages = mutableListOf(
                    ChatMessage(
                        id = "$threadId-divider-today",
                        text = "Today",
                        isOutgoing = false,
                        isDateDivider = true,
                    ),
                    ChatMessage(
                        id = "$threadId-msg-0",
                        text = "Hello! I'm interested in \"${listing.title}\".",
                        isOutgoing = true,
                    ),
                ),
            )
            refreshThreadPreviews()
        }

        return threadId
    }

    private fun refreshThreadPreviews() {
        conversations.values.forEach { conversation ->
            conversation.thread = conversation.thread.withPreview(conversation.messages)
        }
    }

    private fun ConversationFileDto.toState(): ConversationState =
        ConversationState(
            thread = thread.toDomain(),
            messages = messages.map { it.toDomain() }.toMutableList(),
        )

    private fun ChatThread.withPreview(messages: List<ChatMessage>): ChatThread =
        copy(lastMessagePreview = formatLastMessagePreview(messages))

    private fun formatLastMessagePreview(messages: List<ChatMessage>): String {
        val lastMessage = messages.lastOrNull { !it.isDateDivider } ?: return "Нет сообщений"
        val preview = if (lastMessage.text.length > 72) {
            lastMessage.text.take(69) + "..."
        } else {
            lastMessage.text
        }
        return if (lastMessage.isOutgoing) "Вы: $preview" else preview
    }

    private fun avatarColorFor(key: String): Long {
        val colors = listOf(
            0xFFFFB74DL,
            0xFF03989FL,
            0xFF5C9FD6L,
            0xFFFFBF34L,
            0xFFFF8A65L,
            0xFFE57373L,
        )
        val index = key.hashCode().mod(colors.size).let { if (it < 0) it + colors.size else it }
        return colors[index]
    }
}

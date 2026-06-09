package tech.appard.hvala.shared.feature.messages.data.repository

import tech.appard.hvala.shared.core.database.DatabaseSeedKeys
import tech.appard.hvala.shared.core.database.HvalaDatabase
import tech.appard.hvala.shared.core.database.isSeeded
import tech.appard.hvala.shared.core.database.markSeeded
import tech.appard.hvala.shared.core.i18n.PreviewMessage
import tech.appard.hvala.shared.core.i18n.formatLastMessagePreview
import tech.appard.hvala.shared.core.i18n.strings
import tech.appard.hvala.shared.feature.listings.domain.repository.ListingsRepository
import tech.appard.hvala.shared.feature.messages.data.mapper.insertMessage
import tech.appard.hvala.shared.feature.messages.data.mapper.insertThread
import tech.appard.hvala.shared.feature.messages.data.mapper.loadMessages
import tech.appard.hvala.shared.feature.messages.data.mapper.nextMessageSortOrder
import tech.appard.hvala.shared.feature.messages.data.mapper.toChatThread
import tech.appard.hvala.shared.feature.messages.data.mapper.updateThreadPreview
import tech.appard.hvala.shared.feature.messages.data.mapper.toDomain
import tech.appard.hvala.shared.feature.messages.data.source.MessagesJsonDataSource
import tech.appard.hvala.shared.feature.messages.domain.model.ChatMessage
import tech.appard.hvala.shared.feature.messages.domain.model.ChatThread
import tech.appard.hvala.shared.feature.messages.domain.model.PickedMedia
import tech.appard.hvala.shared.feature.messages.domain.repository.MessagesRepository
import tech.appard.hvala.shared.feature.profile.domain.repository.SellerRepository
import tech.appard.hvala.shared.feature.settings.domain.repository.LocaleRepository

internal class JsonMessagesRepository(
    private val database: HvalaDatabase,
    private val dataSource: MessagesJsonDataSource,
    private val listingsRepository: ListingsRepository,
    private val sellerRepository: SellerRepository,
    private val localeRepository: LocaleRepository,
) : MessagesRepository {
    override suspend fun ensureLoaded() {
        if (database.isSeeded(DatabaseSeedKeys.MESSAGES)) return

        listingsRepository.ensureLoaded()
        sellerRepository.ensureLoaded()

        val conversations = dataSource.conversations().conversations
        database.transaction {
            database.chatMessageRowQueries.deleteAll()
            database.chatThreadRowQueries.deleteAll()
            conversations.forEach { conversation ->
                val thread = conversation.thread.toDomain()
                database.insertThread(thread.withPreview(conversation.messages.map { it.toDomain() }))
                conversation.messages.forEachIndexed { index, messageDto ->
                    database.insertMessage(
                        threadId = thread.id,
                        message = messageDto.toDomain(),
                        sortOrder = index,
                    )
                }
            }
            database.markSeeded(DatabaseSeedKeys.MESSAGES)
        }
    }

    override fun getThreads(): List<ChatThread> =
        database.chatThreadRowQueries.selectAll()
            .executeAsList()
            .map { it.toChatThread() }

    override fun getMessages(threadId: String): List<ChatMessage> =
        database.loadMessages(threadId)

    override suspend fun sendMessage(
        threadId: String,
        text: String,
        attachments: List<PickedMedia>,
    ) {
        ensureLoaded()
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

        val sortOrder = database.nextMessageSortOrder(threadId)
        val message = ChatMessage(
            id = "$threadId-msg-$sortOrder",
            text = messageText,
            isOutgoing = true,
        )
        database.insertMessage(threadId, message, sortOrder)

        val preview = formatPreview(database.loadMessages(threadId))
        database.updateThreadPreview(threadId, preview)
    }

    override suspend fun openChatForListing(listingId: String): String? {
        listingsRepository.ensureLoaded()
        sellerRepository.ensureLoaded()
        ensureLoaded()

        val listing = listingsRepository.getListingById(listingId) ?: return null
        val threadId = "listing-$listingId"
        val messagesStrings = localeRepository.getLanguage().strings().messages
        val commonStrings = localeRepository.getLanguage().strings().common

        if (database.chatThreadRowQueries.selectById(threadId).executeAsOneOrNull() == null) {
            val thread = ChatThread(
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
            )
            val messages = listOf(
                ChatMessage(
                    id = "$threadId-divider-today",
                    text = commonStrings.today,
                    isOutgoing = false,
                    isDateDivider = true,
                ),
                ChatMessage(
                    id = "$threadId-msg-0",
                    text = messagesStrings.chatOpener(listing.title),
                    isOutgoing = true,
                ),
            )
            database.insertThread(thread.withPreview(messages))
            messages.forEachIndexed { index, message ->
                database.insertMessage(threadId, message, index)
            }
            val preview = formatPreview(messages)
            database.updateThreadPreview(threadId, preview)
        }

        return threadId
    }

    private fun formatPreview(messages: List<ChatMessage>): String {
        val messagesStrings = localeRepository.getLanguage().strings().messages
        val previewMessages = messages.map { message ->
            PreviewMessage(
                text = message.text,
                isOutgoing = message.isOutgoing,
                isDateDivider = message.isDateDivider,
            )
        }
        return formatLastMessagePreview(previewMessages, messagesStrings)
    }

    private fun ChatThread.withPreview(messages: List<ChatMessage>): ChatThread =
        copy(lastMessagePreview = formatPreview(messages))

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

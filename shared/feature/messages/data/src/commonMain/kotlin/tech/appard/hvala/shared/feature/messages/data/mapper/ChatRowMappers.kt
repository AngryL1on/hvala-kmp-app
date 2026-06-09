package tech.appard.hvala.shared.feature.messages.data.mapper

import tech.appard.hvala.shared.core.database.Chat_message_row
import tech.appard.hvala.shared.core.database.Chat_thread_row
import tech.appard.hvala.shared.core.database.HvalaDatabase
import tech.appard.hvala.shared.feature.messages.domain.model.ChatMessage
import tech.appard.hvala.shared.feature.messages.domain.model.ChatThread

internal fun Chat_thread_row.toChatThread(): ChatThread = ChatThread(
    id = id,
    participantName = participant_name,
    lastMessagePreview = last_message_preview,
    avatarColorArgb = avatar_color_argb,
    listingId = listing_id,
    listingTitle = listing_title,
    listingPriceUsd = listing_price_usd?.toInt(),
    listingPriceRub = listing_price_rub?.toInt(),
    sellerId = seller_id,
)

internal fun ChatThread.toRow(): Chat_thread_row = Chat_thread_row(
    id = id,
    participant_name = participantName,
    last_message_preview = lastMessagePreview,
    avatar_color_argb = avatarColorArgb,
    listing_id = listingId,
    listing_title = listingTitle,
    listing_price_usd = listingPriceUsd?.toLong(),
    listing_price_rub = listingPriceRub?.toLong(),
    seller_id = sellerId,
)

internal fun Chat_message_row.toDomain(): ChatMessage = ChatMessage(
    id = id,
    text = text,
    isOutgoing = is_outgoing == 1L,
    isDateDivider = is_date_divider == 1L,
)

internal fun HvalaDatabase.insertThread(thread: ChatThread) {
    val row = thread.toRow()
    chatThreadRowQueries.insertOrReplace(
        id = row.id,
        participant_name = row.participant_name,
        last_message_preview = row.last_message_preview,
        avatar_color_argb = row.avatar_color_argb,
        listing_id = row.listing_id,
        listing_title = row.listing_title,
        listing_price_usd = row.listing_price_usd,
        listing_price_rub = row.listing_price_rub,
        seller_id = row.seller_id,
    )
}

internal fun HvalaDatabase.updateThreadPreview(threadId: String, preview: String) {
    chatThreadRowQueries.updatePreview(
        last_message_preview = preview,
        id = threadId,
    )
}

internal fun HvalaDatabase.insertMessage(threadId: String, message: ChatMessage, sortOrder: Int) {
    chatMessageRowQueries.insertOrReplace(
        id = message.id,
        thread_id = threadId,
        sort_order = sortOrder.toLong(),
        text = message.text,
        is_outgoing = if (message.isOutgoing) 1L else 0L,
        is_date_divider = if (message.isDateDivider) 1L else 0L,
    )
}

internal fun HvalaDatabase.loadMessages(threadId: String): List<ChatMessage> =
    chatMessageRowQueries.selectForThread(threadId).executeAsList().map { it.toDomain() }

internal fun HvalaDatabase.nextMessageSortOrder(threadId: String): Int =
    chatMessageRowQueries.maxSortOrderForThread(threadId).executeAsOne().toInt() + 1

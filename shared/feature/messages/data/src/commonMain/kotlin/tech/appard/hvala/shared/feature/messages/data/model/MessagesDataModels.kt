package tech.appard.hvala.shared.feature.messages.data.model

import kotlinx.serialization.Serializable

@Serializable
internal data class ConversationFileDto(
    val thread: ChatThreadDto,
    val messages: List<ChatMessageDto>,
)

@Serializable
internal data class ConversationsFileDto(
    val conversations: List<ConversationFileDto>,
)

@Serializable
internal data class ChatMessageDto(
    val id: String,
    val text: String,
    val isOutgoing: Boolean,
    val isDateDivider: Boolean = false,
)

@Serializable
internal data class ChatThreadDto(
    val id: String,
    val participantName: String,
    val lastMessagePreview: String,
    val avatarColorArgb: Long,
    val listingId: String? = null,
    val listingTitle: String? = null,
    val listingPriceUsd: Int? = null,
    val listingPriceRub: Int? = null,
    val sellerId: String? = null,
)

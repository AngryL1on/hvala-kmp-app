package tech.appard.hvala.shared.feature.messages.data.mapper

import tech.appard.hvala.shared.feature.messages.data.model.ChatMessageDto
import tech.appard.hvala.shared.feature.messages.data.model.ChatThreadDto
import tech.appard.hvala.shared.feature.messages.domain.model.ChatMessage
import tech.appard.hvala.shared.feature.messages.domain.model.ChatThread

internal fun ChatMessageDto.toDomain(): ChatMessage = ChatMessage(
    id = id,
    text = text,
    isOutgoing = isOutgoing,
    isDateDivider = isDateDivider,
)

internal fun ChatThreadDto.toDomain(): ChatThread = ChatThread(
    id = id,
    participantName = participantName,
    lastMessagePreview = lastMessagePreview,
    avatarColorArgb = avatarColorArgb,
    listingId = listingId,
    listingTitle = listingTitle,
    listingPriceUsd = listingPriceUsd,
    listingPriceRub = listingPriceRub,
    sellerId = sellerId,
)

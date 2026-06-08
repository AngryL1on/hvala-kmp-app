package tech.appard.hvala.shared.feature.messages.ui.mapper

import tech.appard.hvala.shared.core.ui.model.PickedMedia as PlatformPickedMedia
import tech.appard.hvala.shared.feature.messages.domain.model.ChatMessage
import tech.appard.hvala.shared.feature.messages.domain.model.ChatThread
import tech.appard.hvala.shared.feature.messages.domain.model.PickedMedia
import tech.appard.hvala.shared.feature.messages.domain.model.resolvedListingId
import tech.appard.hvala.shared.feature.messages.domain.model.resolvedSellerId
import tech.appard.hvala.shared.feature.messages.ui.model.UIChatMessage
import tech.appard.hvala.shared.feature.messages.ui.model.UIChatThread

fun ChatMessage.toUi(): UIChatMessage = UIChatMessage(
    id = id,
    text = text,
    isOutgoing = isOutgoing,
    isDateDivider = isDateDivider,
)

fun UIChatMessage.toDomain(): ChatMessage = ChatMessage(
    id = id,
    text = text,
    isOutgoing = isOutgoing,
    isDateDivider = isDateDivider,
)

fun ChatThread.toUi(): UIChatThread = UIChatThread(
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

fun UIChatThread.toDomain(): ChatThread = ChatThread(
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

fun UIChatThread.resolvedListingId(): String? = toDomain().resolvedListingId()

fun UIChatThread.resolvedSellerId(): String? = toDomain().resolvedSellerId()

fun List<ChatMessage>.toMessagesUi(): List<UIChatMessage> = map { it.toUi() }

fun List<ChatThread>.toThreadsUi(): List<UIChatThread> = map { it.toUi() }

fun PlatformPickedMedia.toDomain(): PickedMedia = PickedMedia(
    uri = uri,
    name = name,
    mimeType = mimeType,
)

fun List<PlatformPickedMedia>.toDomain(): List<PickedMedia> = map { it.toDomain() }

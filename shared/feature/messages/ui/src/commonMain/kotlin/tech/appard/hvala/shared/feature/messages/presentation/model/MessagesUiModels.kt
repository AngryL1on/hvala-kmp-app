package tech.appard.hvala.shared.feature.messages.presentation.model

data class UIChatMessage(
    val id: String,
    val text: String,
    val isOutgoing: Boolean,
    val isDateDivider: Boolean = false,
)

data class UIChatThread(
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

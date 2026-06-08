package tech.appard.hvala.shared.core.contracts.model

data class ChatThread(
    val id: String,
    val participantName: String,
    val lastMessagePreview: String,
    val avatarColorArgb: Long,
    val listingTitle: String? = null,
    val listingPriceUsd: Int? = null,
    val listingPriceRub: Int? = null,
)

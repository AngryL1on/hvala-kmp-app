package tech.appard.hvala.shared.core.contracts.model

data class ChatThread(
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

fun ChatThread.resolvedListingId(): String? {
    listingId?.let { return it }
    if (id.startsWith("listing-")) {
        return id.removePrefix("listing-")
    }
    return null
}

fun ChatThread.resolvedSellerId(): String? {
    sellerId?.let { return it }
    if (id.startsWith("listing-")) return null
    return id
}

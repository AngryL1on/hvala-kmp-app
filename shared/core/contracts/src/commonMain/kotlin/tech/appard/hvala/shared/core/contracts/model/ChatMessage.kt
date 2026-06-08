package tech.appard.hvala.shared.core.contracts.model

data class ChatMessage(
    val id: String,
    val text: String,
    val isOutgoing: Boolean,
    val isDateDivider: Boolean = false,
)

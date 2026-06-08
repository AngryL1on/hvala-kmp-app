package tech.appard.hvala.shared.feature.messages.domain.model

data class ChatMessage(
    val id: String,
    val text: String,
    val isOutgoing: Boolean,
    val isDateDivider: Boolean = false,
)

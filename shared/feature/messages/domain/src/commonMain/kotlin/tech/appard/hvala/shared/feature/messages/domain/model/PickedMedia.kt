package tech.appard.hvala.shared.feature.messages.domain.model

data class PickedMedia(
    val uri: String,
    val name: String,
    val mimeType: String? = null,
)

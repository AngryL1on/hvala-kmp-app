package tech.appard.hvala.shared.core.contracts.model

data class PickedMedia(
    val uri: String,
    val name: String,
    val mimeType: String? = null,
    val isImage: Boolean = false,
)

enum class MediaPickerMode {
    Images,
    Files,
}

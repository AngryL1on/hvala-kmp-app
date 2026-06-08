package tech.appard.hvala.shared.core.ui.model

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

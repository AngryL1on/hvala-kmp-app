package tech.appard.hvala.shared.core.ui.platform

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSFileManager
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.NSURL
import platform.PhotosUI.PHPickerConfiguration
import platform.PhotosUI.PHPickerFilter
import platform.PhotosUI.PHPickerResult
import platform.PhotosUI.PHPickerViewController
import platform.PhotosUI.PHPickerViewControllerDelegateProtocol
import platform.UIKit.UIDocumentPickerDelegateProtocol
import platform.UIKit.UIDocumentPickerViewController
import platform.UIKit.UIDocumentPickerMode
import platform.UIKit.UIViewController
import platform.UniformTypeIdentifiers.UTTypeImage
import platform.darwin.NSObject
import platform.darwin.dispatch_get_main_queue
import platform.darwin.dispatch_group_create
import platform.darwin.dispatch_group_enter
import platform.darwin.dispatch_group_leave
import platform.darwin.dispatch_group_notify
import tech.appard.hvala.shared.core.ui.model.PickedMedia

@OptIn(ExperimentalForeignApi::class)
internal class IosPhotoPickerDelegate(
    private val onComplete: (List<PickedMedia>) -> Unit,
) : NSObject(), PHPickerViewControllerDelegateProtocol {

    fun present(viewController: UIViewController, maxItems: Int) {
        val configuration = PHPickerConfiguration()
        configuration.filter = PHPickerFilter.imagesFilter
        configuration.selectionLimit = maxItems.coerceAtLeast(1).toLong()

        val picker = PHPickerViewController(configuration)
        picker.delegate = this
        viewController.presentViewController(picker, animated = true, completion = null)
    }

    override fun picker(
        picker: PHPickerViewController,
        didFinishPicking: List<*>,
    ) {
        picker.dismissViewControllerAnimated(true, completion = null)

        val results = didFinishPicking.filterIsInstance<PHPickerResult>()
        if (results.isEmpty()) {
            onComplete(emptyList())
            return
        }

        val picked = mutableListOf<PickedMedia>()
        val group = dispatch_group_create()

        results.forEach { result ->
            dispatch_group_enter(group)
            result.itemProvider.loadFileRepresentationForTypeIdentifier(UTTypeImage.identifier) { url, _ ->
                if (url != null) {
                    url.toPickedMedia(isImage = true)?.let(picked::add)
                }
                dispatch_group_leave(group)
            }
        }

        dispatch_group_notify(group, dispatch_get_main_queue()) {
            onComplete(picked.toList())
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
internal class IosDocumentPickerDelegate(
    private val onComplete: (List<PickedMedia>) -> Unit,
) : NSObject(), UIDocumentPickerDelegateProtocol {

    fun present(viewController: UIViewController, maxItems: Int) {
        val picker = UIDocumentPickerViewController(
            documentTypes = listOf("public.item"),
            inMode = UIDocumentPickerMode.UIDocumentPickerModeImport,
        )
        picker.allowsMultipleSelection = maxItems > 1
        picker.delegate = this
        viewController.presentViewController(picker, animated = true, completion = null)
    }

    override fun documentPicker(
        controller: UIDocumentPickerViewController,
        didPickDocumentsAtURLs: List<*>,
    ) {
        val picked = didPickDocumentsAtURLs
            .filterIsInstance<NSURL>()
            .mapNotNull { url ->
                val accessed = url.startAccessingSecurityScopedResource()
                val media = url.toPickedMedia(
                    isImage = (url.pathExtension as String?)?.lowercase() in imageExtensions,
                )
                if (accessed) {
                    url.stopAccessingSecurityScopedResource()
                }
                media
            }
        onComplete(picked)
    }

    override fun documentPickerWasCancelled(controller: UIDocumentPickerViewController) {
        onComplete(emptyList())
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun NSURL.toPickedMedia(isImage: Boolean): PickedMedia? {
    val fileName = lastPathComponent as? String ?: return null
    val copiedPath = copyToTemporaryDirectory(this) ?: path ?: return null
    return PickedMedia(
        uri = copiedPath,
        name = fileName,
        mimeType = mimeTypeForExtension(pathExtension as? String),
        isImage = isImage,
    )
}

@OptIn(ExperimentalForeignApi::class)
private fun copyToTemporaryDirectory(sourceUrl: NSURL): String? {
    val fileManager = NSFileManager.defaultManager
    val fileName = sourceUrl.lastPathComponent as? String ?: return null
    val destinationPath = "${NSTemporaryDirectory()}hvala-${kotlin.random.Random.nextLong()}-$fileName"
    val destinationUrl = NSURL.fileURLWithPath(destinationPath)
    return if (fileManager.copyItemAtURL(sourceUrl, destinationUrl, error = null)) {
        destinationPath
    } else {
        sourceUrl.path
    }
}

private val imageExtensions = setOf("jpg", "jpeg", "png", "gif", "webp", "heic", "heif")

private fun mimeTypeForExtension(extension: String?): String? = when (extension?.lowercase()) {
    "jpg", "jpeg" -> "image/jpeg"
    "png" -> "image/png"
    "gif" -> "image/gif"
    "webp" -> "image/webp"
    "heic" -> "image/heic"
    "heif" -> "image/heif"
    "pdf" -> "application/pdf"
    else -> null
}

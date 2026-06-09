package tech.appard.hvala.shared.core.ui.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.interop.LocalUIViewController
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.NSUUID
import platform.Foundation.create
import platform.Foundation.writeToFile
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.UIKit.UIImagePickerController
import platform.UIKit.UIImagePickerControllerDelegateProtocol
import platform.UIKit.UIImagePickerControllerOriginalImage
import platform.UIKit.UIImagePickerControllerSourceType
import platform.UIKit.UINavigationControllerDelegateProtocol
import platform.UIKit.UIViewController
import platform.darwin.NSObject

@Composable
actual fun rememberImagePicker(): ImagePicker {
    val viewController = LocalUIViewController.current
    return remember(viewController) {
        IosImagePicker(viewController)
    }
}

private class IosImagePicker(
    private val viewController: UIViewController,
) : ImagePicker {
    private var callback: ((String?) -> Unit)? = null
    private var delegate: PickerDelegate? = null

    override fun pick(onResult: (String?) -> Unit) {
        callback = onResult
        val picker = UIImagePickerController().apply {
            sourceType = UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypePhotoLibrary
            delegate = PickerDelegate(
                onPicked = { path ->
                    callback?.invoke(path)
                    callback = null
                    delegate = null
                },
            ).also { delegate = it }
        }
        viewController.presentViewController(picker, animated = true, completion = null)
    }
}

private class PickerDelegate(
    private val onPicked: (String?) -> Unit,
) : NSObject(), UIImagePickerControllerDelegateProtocol, UINavigationControllerDelegateProtocol {

    override fun imagePickerController(
        picker: UIImagePickerController,
        didFinishPickingMediaWithInfo: Map<Any?, *>,
    ) {
        picker.dismissViewControllerAnimated(true, completion = null)
        val image = didFinishPickingMediaWithInfo[UIImagePickerControllerOriginalImage] as? UIImage
        onPicked(image?.let(::saveTemporaryJpeg))
    }

    override fun imagePickerControllerDidCancel(picker: UIImagePickerController) {
        picker.dismissViewControllerAnimated(true, completion = null)
        onPicked(null)
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun saveTemporaryJpeg(image: UIImage): String? {
    val data = UIImageJPEGRepresentation(image, 0.9) ?: return null
    val path = NSTemporaryDirectory() + "avatar-${NSUUID().UUIDString}.jpg"
    return if (data.writeToFile(path, atomically = true)) "file://$path" else null
}

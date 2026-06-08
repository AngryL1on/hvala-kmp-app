package tech.appard.hvala.shared.core.ui.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.uikit.LocalUIViewController
import platform.UIKit.UIViewController
import tech.appard.hvala.shared.core.ui.model.MediaPickerMode
import tech.appard.hvala.shared.core.ui.model.PickedMedia

@Composable
actual fun rememberMediaPickerLauncher(
    mode: MediaPickerMode,
    onResult: (List<PickedMedia>) -> Unit,
): MediaPickerLauncher {
    val viewController = LocalUIViewController.current
    val onResultState = rememberUpdatedState(onResult)

    return remember(mode, viewController) {
        MediaPickerLauncher { maxItems ->
            when (mode) {
                MediaPickerMode.Images -> {
                    IosMediaPickerPresenter.presentPhotoPicker(
                        viewController = viewController,
                        maxItems = maxItems,
                        onResult = onResultState.value,
                    )
                }
                MediaPickerMode.Files -> {
                    IosMediaPickerPresenter.presentDocumentPicker(
                        viewController = viewController,
                        maxItems = maxItems,
                        onResult = onResultState.value,
                    )
                }
            }
        }
    }
}

internal object IosMediaPickerPresenter {
    private val activeDelegates = mutableListOf<Any>()

    fun retainDelegate(delegate: Any) {
        activeDelegates.add(delegate)
    }

    fun releaseDelegate(delegate: Any) {
        activeDelegates.remove(delegate)
    }

    fun presentPhotoPicker(
        viewController: UIViewController,
        maxItems: Int,
        onResult: (List<PickedMedia>) -> Unit,
    ) {
        lateinit var delegateRef: IosPhotoPickerDelegate
        delegateRef = IosPhotoPickerDelegate { picked ->
            releaseDelegate(delegateRef)
            onResult(picked)
        }
        retainDelegate(delegateRef)
        delegateRef.present(viewController = viewController, maxItems = maxItems)
    }

    fun presentDocumentPicker(
        viewController: UIViewController,
        maxItems: Int,
        onResult: (List<PickedMedia>) -> Unit,
    ) {
        lateinit var delegateRef: IosDocumentPickerDelegate
        delegateRef = IosDocumentPickerDelegate { picked ->
            releaseDelegate(delegateRef)
            onResult(picked)
        }
        retainDelegate(delegateRef)
        delegateRef.present(viewController = viewController, maxItems = maxItems)
    }
}

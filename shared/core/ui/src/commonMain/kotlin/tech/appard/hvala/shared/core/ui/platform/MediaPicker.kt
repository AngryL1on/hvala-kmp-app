package tech.appard.hvala.shared.core.ui.platform

import androidx.compose.runtime.Composable
import tech.appard.hvala.shared.core.contracts.model.MediaPickerMode
import tech.appard.hvala.shared.core.contracts.model.PickedMedia

class MediaPickerLauncher(
    private val launchPicker: (maxItems: Int) -> Unit,
) {
    fun launch(maxItems: Int = Int.MAX_VALUE) {
        launchPicker(maxItems)
    }
}

@Composable
expect fun rememberMediaPickerLauncher(
    mode: MediaPickerMode,
    onResult: (List<PickedMedia>) -> Unit,
): MediaPickerLauncher

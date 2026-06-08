package tech.appard.hvala.shared.core.ui.platform

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import tech.appard.hvala.shared.core.contracts.model.MediaPickerMode
import tech.appard.hvala.shared.core.contracts.model.PickedMedia

@Composable
actual fun rememberMediaPickerLauncher(
    mode: MediaPickerMode,
    onResult: (List<PickedMedia>) -> Unit,
): MediaPickerLauncher {
    val context = LocalContext.current
    val onResultState = rememberUpdatedState(onResult)

    val multipleImagesLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
    ) { uris ->
        onResultState.value(uris.map { uri -> uri.toPickedMedia(context) })
    }

    val singleImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
    ) { uri ->
        onResultState.value(
            uri?.let { listOf(it.toPickedMedia(context)) }.orEmpty(),
        )
    }

    val multipleFilesLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments(),
    ) { uris ->
        onResultState.value(uris.map { uri -> uri.toPickedMedia(context) })
    }

    val singleFileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
    ) { uri ->
        onResultState.value(
            uri?.let { listOf(it.toPickedMedia(context)) }.orEmpty(),
        )
    }

    return remember(mode) {
        MediaPickerLauncher { maxItems ->
            when (mode) {
                MediaPickerMode.Images -> {
                    val request = PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    if (maxItems <= 1) {
                        singleImageLauncher.launch(request)
                    } else {
                        multipleImagesLauncher.launch(request)
                    }
                }
                MediaPickerMode.Files -> {
                    if (maxItems <= 1) {
                        singleFileLauncher.launch(arrayOf("*/*"))
                    } else {
                        multipleFilesLauncher.launch(arrayOf("*/*"))
                    }
                }
            }
        }
    }
}

private fun Uri.toPickedMedia(context: Context): PickedMedia {
    val mimeType = context.contentResolver.getType(this)
    return PickedMedia(
        uri = toString(),
        name = context.resolveDisplayName(this),
        mimeType = mimeType,
        isImage = mimeType?.startsWith("image/") == true,
    )
}

private fun Context.resolveDisplayName(uri: Uri): String {
    contentResolver.query(uri, null, null, null, null)?.use { cursor ->
        val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        if (nameIndex >= 0 && cursor.moveToFirst()) {
            return cursor.getString(nameIndex)
        }
    }
    return uri.lastPathSegment ?: "file"
}

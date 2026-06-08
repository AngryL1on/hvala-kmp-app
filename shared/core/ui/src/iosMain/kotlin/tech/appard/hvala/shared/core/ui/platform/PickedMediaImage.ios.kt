package tech.appard.hvala.shared.core.ui.platform

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.InsertDriveFile
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.layout.ContentScale
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import org.jetbrains.skia.Image
import platform.Foundation.NSData
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.UIKit.UIImagePNGRepresentation
import platform.posix.memcpy
import tech.appard.hvala.shared.core.contracts.model.PickedMedia
import tech.appard.hvala.shared.core.ui.theme.GrayPlaceholder
import tech.appard.hvala.shared.core.ui.theme.LocalDimensions

@Composable
actual fun PickedMediaImage(
    media: PickedMedia,
    modifier: Modifier,
    contentDescription: String?,
) {
    var imageBitmap by remember(media.uri) { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(media.uri, media.isImage) {
        imageBitmap = if (media.isImage) {
            loadImageFromPath(media.uri)
        } else {
            null
        }
    }

    if (imageBitmap != null) {
        Image(
            bitmap = imageBitmap!!,
            contentDescription = contentDescription ?: media.name,
            modifier = modifier,
            contentScale = ContentScale.Crop,
        )
    } else {
        FilePlaceholder(modifier = modifier)
    }
}

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
private fun loadImageFromPath(path: String): ImageBitmap? {
    val uiImage = UIImage.imageWithContentsOfFile(path) ?: return null
    val data = UIImageJPEGRepresentation(uiImage, 1.0) ?: UIImagePNGRepresentation(uiImage) ?: return null
    return runCatching {
        Image.makeFromEncoded(data.toByteArray()).toComposeImageBitmap()
    }.getOrNull()
}

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
private fun NSData.toByteArray(): ByteArray {
    val size = length.toInt()
    if (size == 0) return ByteArray(0)
    return ByteArray(size).apply {
        usePinned { pinned ->
            memcpy(pinned.addressOf(0), bytes, length)
        }
    }
}

@Composable
private fun FilePlaceholder(modifier: Modifier = Modifier) {
    val dimensions = LocalDimensions.current

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Outlined.InsertDriveFile,
            contentDescription = null,
            tint = GrayPlaceholder,
            modifier = Modifier.fillMaxSize(fraction = 0.45f),
        )
    }
}

package tech.appard.hvala.shared.core.ui.components.image

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.layout.ContentScale
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import org.jetbrains.skia.Data
import org.jetbrains.skia.Image as SkiaImage
import platform.Foundation.NSData
import platform.Foundation.NSURL
import platform.UIKit.UIImage
import platform.UIKit.UIImagePNGRepresentation
import platform.posix.memcpy

@Composable
actual fun LocalUriImage(
    uri: String?,
    modifier: Modifier,
    contentScale: ContentScale,
    contentDescription: String?,
) {
    if (uri.isNullOrBlank()) return

    val imageBitmap = remember(uri) { loadImageBitmap(uri) }
    imageBitmap?.let {
        Image(
            bitmap = it,
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = contentScale,
        )
    }
}

private fun loadImageBitmap(uri: String) = runCatching {
    val uiImage = when {
        uri.startsWith("file://") -> UIImage.imageWithContentsOfFile(uri.removePrefix("file://"))
        else -> NSURL.URLWithString(uri)?.let { url ->
            NSData.dataWithContentsOfURL(url)?.let { data ->
                UIImage.imageWithData(data)
            }
        }
    } ?: return@runCatching null

    val pngData = UIImagePNGRepresentation(uiImage) ?: return@runCatching null
    SkiaImage.makeFromEncoded(Data.makeFromBytes(pngData.toByteArray())).toComposeImageBitmap()
}.getOrNull()

@OptIn(ExperimentalForeignApi::class)
private fun NSData.toByteArray(): ByteArray {
    val length = this.length.toInt()
    if (length == 0) return ByteArray(0)
    return ByteArray(length).apply {
        usePinned { pinned ->
            memcpy(pinned.addressOf(0), bytes, length.toULong())
        }
    }
}

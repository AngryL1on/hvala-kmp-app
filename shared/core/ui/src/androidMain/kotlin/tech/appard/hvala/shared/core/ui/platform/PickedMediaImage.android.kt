package tech.appard.hvala.shared.core.ui.platform

import android.graphics.BitmapFactory
import android.net.Uri
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tech.appard.hvala.shared.core.ui.model.PickedMedia
import tech.appard.hvala.shared.core.ui.theme.GrayPlaceholder
import tech.appard.hvala.shared.core.ui.theme.LocalDimensions

@Composable
actual fun PickedMediaImage(
    media: PickedMedia,
    modifier: Modifier,
    contentDescription: String?,
) {
    val context = LocalContext.current
    var imageBitmap by remember(media.uri) { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(media.uri, media.isImage) {
        if (!media.isImage) {
            imageBitmap = null
            return@LaunchedEffect
        }
        imageBitmap = withContext(Dispatchers.IO) {
            runCatching {
                context.contentResolver.openInputStream(Uri.parse(media.uri))?.use { stream ->
                    BitmapFactory.decodeStream(stream)?.asImageBitmap()
                }
            }.getOrNull()
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

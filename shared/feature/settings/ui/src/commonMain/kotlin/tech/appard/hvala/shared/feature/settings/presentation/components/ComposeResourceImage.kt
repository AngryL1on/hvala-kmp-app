package tech.appard.hvala.shared.feature.settings.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.decodeToImageBitmap
import tech.appard.hvala.shared.feature.settings.ui.resources.Res

@OptIn(ExperimentalResourceApi::class)
@Composable
internal fun rememberComposeResourceImage(path: String): ImageBitmap? {
    var image by remember(path) { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(path) {
        runCatching {
            Res.readBytes(path).decodeToImageBitmap()
        }.onSuccess { decoded ->
            image = decoded
        }
    }

    return image
}

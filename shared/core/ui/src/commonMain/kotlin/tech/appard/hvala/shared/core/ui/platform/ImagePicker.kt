package tech.appard.hvala.shared.core.ui.platform

import androidx.compose.runtime.Composable

fun interface ImagePicker {
    fun pick(onResult: (String?) -> Unit)
}

@Composable
expect fun rememberImagePicker(): ImagePicker

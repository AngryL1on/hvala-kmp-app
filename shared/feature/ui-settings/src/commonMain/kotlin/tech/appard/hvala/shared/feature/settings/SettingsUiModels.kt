package tech.appard.hvala.shared.feature.settings

import androidx.compose.ui.graphics.vector.ImageVector

data class SettingsMenuItem(
    val id: String,
    val title: String,
    val icon: ImageVector,
    val isDestructive: Boolean = false,
)

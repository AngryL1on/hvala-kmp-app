package tech.appard.hvala.shared.core.ui.platform

import androidx.compose.runtime.Composable

interface ExternalLinkOpener {
    fun openUrl(url: String)
    fun openEmail(email: String)
}

@Composable
expect fun rememberExternalLinkOpener(): ExternalLinkOpener

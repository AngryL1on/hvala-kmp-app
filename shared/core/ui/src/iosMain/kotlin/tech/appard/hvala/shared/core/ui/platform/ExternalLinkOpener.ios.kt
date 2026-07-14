package tech.appard.hvala.shared.core.ui.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.Foundation.NSURL
import platform.UIKit.UIApplication

@Composable
actual fun rememberExternalLinkOpener(): ExternalLinkOpener {
    return remember {
        object : ExternalLinkOpener {
            override fun openUrl(url: String) {
                if (url.isBlank()) return
                NSURL.URLWithString(url)?.let { nsUrl ->
                    UIApplication.sharedApplication.openURL(nsUrl)
                }
            }

            override fun openEmail(email: String) {
                if (email.isBlank()) return
                openUrl("mailto:$email")
            }
        }
    }
}

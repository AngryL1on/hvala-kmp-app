package tech.appard.hvala.shared.core.ui.platform

import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun rememberExternalLinkOpener(): ExternalLinkOpener {
    val context = LocalContext.current

    return remember(context) {
        object : ExternalLinkOpener {
            override fun openUrl(url: String) {
                if (url.isBlank()) return
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            }

            override fun openEmail(email: String) {
                if (email.isBlank()) return
                val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$email"))
                context.startActivity(intent)
            }
        }
    }
}

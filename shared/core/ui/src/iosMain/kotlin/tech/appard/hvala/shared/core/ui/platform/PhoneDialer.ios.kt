package tech.appard.hvala.shared.core.ui.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.Foundation.NSURL
import platform.UIKit.UIApplication

@Composable
actual fun rememberPhoneDialer(): PhoneDialer {
    return remember {
        object : PhoneDialer {
            override fun dial(phoneNumber: String) {
                val normalized = phoneNumber.toDialNumber()
                if (normalized.isBlank()) return
                val url = NSURL.URLWithString("tel:$normalized") ?: return
                UIApplication.sharedApplication.openURL(url)
            }
        }
    }
}

package tech.appard.hvala.shared.core.ui.platform

import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun rememberPhoneDialer(): PhoneDialer {
    val context = LocalContext.current

    return remember(context) {
        object : PhoneDialer {
            override fun dial(phoneNumber: String) {
                val normalized = phoneNumber.toDialNumber()
                if (normalized.isBlank()) return
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$normalized"))
                context.startActivity(intent)
            }
        }
    }
}

package tech.appard.hvala.shared.core.ui.platform

import androidx.compose.runtime.Composable

interface PhoneDialer {
    fun dial(phoneNumber: String)
}

fun String.toDialNumber(): String =
    filter { it.isDigit() || it == '+' }

@Composable
expect fun rememberPhoneDialer(): PhoneDialer

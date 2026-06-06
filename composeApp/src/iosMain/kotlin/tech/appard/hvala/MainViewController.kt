package tech.appard.hvala

import androidx.compose.ui.window.ComposeUIViewController
import tech.appard.hvala.di.initKoin

fun MainViewController() = ComposeUIViewController {
    initKoin()
    App()
}

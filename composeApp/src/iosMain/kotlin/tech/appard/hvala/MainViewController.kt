package tech.appard.hvala

import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController
import tech.appard.hvala.di.initKoin

fun MainViewController(): UIViewController {
    initKoin()
    return ComposeUIViewController { App() }
}

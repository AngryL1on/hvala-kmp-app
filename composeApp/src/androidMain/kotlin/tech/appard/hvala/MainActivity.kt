package tech.appard.hvala

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import tech.appard.hvala.di.initKoin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                scrim = Color.WHITE,
                darkScrim = Color.WHITE,
            ),
            navigationBarStyle = SystemBarStyle.light(
                scrim = Color.WHITE,
                darkScrim = Color.WHITE,
            ),
        )
        initKoin()

        super.onCreate(savedInstanceState)

        setContent {
            App()
        }
    }
}

@Composable
@Preview
fun AppAndroidPreview() {
    App()
}

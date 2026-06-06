package tech.appard.hvala

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import tech.appard.hvala.di.initKoin
import tech.appard.hvala.navigation.AppNavHost
import tech.appard.hvala.navigation.Route
import tech.appard.hvala.navigation.rememberNavController
import tech.appard.hvala.shared.core.ui.theme.HvalaTheme

@Composable
@Preview
fun App() {
    initKoin()

    HvalaTheme {
        Surface {
            val navController = rememberNavController(initialRoute = Route.Auth)

            AppNavHost(navController = navController)
        }
    }
}

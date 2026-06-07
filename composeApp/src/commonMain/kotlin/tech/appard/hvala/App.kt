package tech.appard.hvala

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import tech.appard.hvala.navigation.AppNavHost
import tech.appard.hvala.navigation.Route
import tech.appard.hvala.navigation.rememberNavController
import tech.appard.hvala.shared.core.ui.theme.HvalaTheme

@Composable
fun App() {
    HvalaTheme {
        Surface {
            val navController = rememberNavController(initialRoute = Route.Auth)

            AppNavHost(navController = navController)
        }
    }
}

package tech.appard.hvala

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
        val navController = rememberNavController(initialRoute = Route.Auth)

        AppNavHost(
            navController = navController,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

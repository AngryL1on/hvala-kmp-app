package tech.appard.hvala

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import tech.appard.hvala.di.initKoin
import tech.appard.hvala.navigation.AppNavHost
import tech.appard.hvala.navigation.Route
import tech.appard.hvala.navigation.rememberNavController
import tech.appard.hvala.shared.core.i18n.ProvideAppLocalization
import tech.appard.hvala.shared.core.ui.theme.HvalaTheme
import tech.appard.hvala.shared.feature.settings.domain.repository.LocaleRepository

@Composable
@Preview
fun App() {
    initKoin()

    val localeRepository = koinInject<LocaleRepository>()
    val language by localeRepository.languageFlow.collectAsState()

    ProvideAppLocalization(language) {
        HvalaTheme {
            val navController = rememberNavController(initialRoute = Route.Listings)

            AppNavHost(
                navController = navController,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

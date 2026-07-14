package tech.appard.hvala.shared.feature.settings.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import tech.appard.hvala.shared.core.i18n.AppLanguage
import tech.appard.hvala.shared.core.i18n.ProvideAppLocalization
import tech.appard.hvala.shared.core.i18n.appStrings
import tech.appard.hvala.shared.core.ui.theme.BodyMedium
import tech.appard.hvala.shared.core.ui.theme.GrayText
import tech.appard.hvala.shared.core.ui.theme.HvalaTheme
import tech.appard.hvala.shared.core.ui.theme.LocalDimensions
import tech.appard.hvala.shared.core.ui.theme.ScreenBackground

@Composable
fun PrivacyPolicyScreen(
    modifier: Modifier = Modifier,
) {
    val dimensions = LocalDimensions.current
    val strings = appStrings().privacyPolicy

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ScreenBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = dimensions.horizontalMedium)
            .padding(vertical = dimensions.verticalMedium),
    ) {
        Text(
            text = strings.body,
            style = BodyMedium.copy(color = GrayText),
        )
    }
}

@Composable
@Preview
private fun PrivacyPolicyScreenPreview() {
    HvalaTheme {
        ProvideAppLocalization(AppLanguage.RU) {
            PrivacyPolicyScreen()
        }
    }
}

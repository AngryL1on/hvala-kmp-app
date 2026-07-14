package tech.appard.hvala.shared.feature.settings.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import tech.appard.hvala.shared.core.i18n.AppLanguage
import tech.appard.hvala.shared.core.i18n.ProvideAppLocalization
import tech.appard.hvala.shared.core.i18n.appStrings
import tech.appard.hvala.shared.core.ui.platform.rememberExternalLinkOpener
import tech.appard.hvala.shared.core.ui.theme.BodyMedium
import tech.appard.hvala.shared.core.ui.theme.GrayText
import tech.appard.hvala.shared.core.ui.theme.HvalaTheme
import tech.appard.hvala.shared.core.ui.theme.LocalDimensions
import tech.appard.hvala.shared.core.ui.theme.PrimaryMain
import tech.appard.hvala.shared.core.ui.theme.ScreenBackground
import tech.appard.hvala.shared.feature.settings.presentation.components.rememberComposeResourceImage

private const val INSTAGRAM_URL = "https://www.instagram.com/hvala.app?igsh=ZmJiOWtrbDY1enNm"
private const val FACEBOOK_URL = "https://www.facebook.com/profile.php?id=61553691035329&mibextid=ZbWKwL"

@Composable
fun ContactsScreen(
    modifier: Modifier = Modifier,
) {
    val dimensions = LocalDimensions.current
    val strings = appStrings().contacts
    val linkOpener = rememberExternalLinkOpener()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ScreenBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = dimensions.horizontalMedium)
            .padding(vertical = dimensions.verticalMedium),
        verticalArrangement = Arrangement.spacedBy(dimensions.verticalMedium),
    ) {
        Text(
            text = strings.description,
            style = BodyMedium.copy(color = GrayText),
        )

        ContactEmailRow(
            label = strings.emailLabel,
            email = strings.supportEmail,
            onClick = { linkOpener.openEmail(strings.supportEmail) },
        )

        ContactEmailRow(
            label = strings.emailLabel,
            email = strings.infoEmail,
            onClick = { linkOpener.openEmail(strings.infoEmail) },
        )

        Text(
            text = strings.socialNetworks,
            style = BodyMedium.copy(color = GrayText),
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(
                dimensions.horizontalLarge,
                Alignment.CenterHorizontally,
            ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val instagramIcon = rememberComposeResourceImage("files/instagram_icon.png")
            val facebookIcon = rememberComposeResourceImage("files/facebook_icon.png")
            instagramIcon?.let { image ->
                Image(
                    bitmap = image,
                    contentDescription = "Instagram",
                    modifier = Modifier
                        .size(dimensions.settingsAvatarSize)
                        .clickable { linkOpener.openUrl(INSTAGRAM_URL) },
                )
            }
            facebookIcon?.let { image ->
                Image(
                    bitmap = image,
                    contentDescription = "Facebook",
                    modifier = Modifier
                        .size(dimensions.settingsAvatarSize)
                        .clickable { linkOpener.openUrl(FACEBOOK_URL) },
                )
            }
        }
    }
}

@Composable
private fun ContactEmailRow(
    label: String,
    email: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    ) {
        Text(
            text = label,
            style = BodyMedium.copy(color = GrayText),
        )
        Text(
            text = email,
            style = BodyMedium.copy(color = PrimaryMain),
        )
    }
}

@Composable
@Preview
private fun ContactsScreenPreview() {
    HvalaTheme {
        ProvideAppLocalization(AppLanguage.RU) {
            ContactsScreen()
        }
    }
}

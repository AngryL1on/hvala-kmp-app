package tech.appard.hvala.shared.feature.settings.presentation.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import tech.appard.hvala.shared.core.i18n.AppLanguage
import tech.appard.hvala.shared.core.i18n.ProvideAppLocalization
import tech.appard.hvala.shared.core.i18n.appLanguage
import tech.appard.hvala.shared.core.i18n.appStrings
import tech.appard.hvala.shared.core.ui.theme.BodyMedium
import tech.appard.hvala.shared.core.ui.theme.GrayText
import tech.appard.hvala.shared.core.ui.theme.HvalaTheme
import tech.appard.hvala.shared.core.ui.theme.LocalDimensions
import tech.appard.hvala.shared.core.ui.theme.PrimaryMain
import tech.appard.hvala.shared.core.ui.theme.ScreenBackground
import tech.appard.hvala.shared.feature.settings.presentation.components.rememberComposeResourceImage

private fun informationImagePaths(language: AppLanguage): List<String> = when (language) {
    AppLanguage.RU -> listOf(
        "files/faq_1_rus.jpg",
        "files/faq_2_rus.jpg",
        "files/faq_3_rus.jpg",
        "files/faq_4_rus.jpg",
    )
    AppLanguage.EN -> listOf(
        "files/faq_1_eng.jpg",
        "files/faq_2_eng.jpg",
        "files/faq_3_eng.jpg",
        "files/faq_4_eng.jpg",
    )
    AppLanguage.SR,
    AppLanguage.CNR,
    AppLanguage.BS,
    AppLanguage.HR,
    -> listOf(
        "files/faq_1_crno.jpg",
        "files/faq_2_crno.jpg",
        "files/faq_3_crno.jpg",
        "files/faq_4_crno.jpg",
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun InformationScreen(
    modifier: Modifier = Modifier,
) {
    val dimensions = LocalDimensions.current
    val strings = appStrings().information
    val imagePaths = informationImagePaths(appLanguage())
    val pagerState = rememberPagerState(pageCount = { imagePaths.size })

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ScreenBackground)
            .padding(horizontal = dimensions.horizontalMedium)
            .padding(vertical = dimensions.verticalMedium),
        verticalArrangement = Arrangement.spacedBy(dimensions.verticalMedium),
    ) {
        Text(
            text = strings.chooseLanguage,
            style = BodyMedium.copy(color = GrayText),
            modifier = Modifier.fillMaxWidth(),
        )

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensions.settingsAvatarSize * 4),
        ) { page ->
            InformationImage(path = imagePaths[page])
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            repeat(imagePaths.size) { index ->
                Box(
                    modifier = Modifier
                        .padding(horizontal = dimensions.horizontalXXSmall)
                        .size(dimensions.horizontalXSmall)
                        .clip(CircleShape)
                        .background(
                            if (pagerState.currentPage == index) PrimaryMain else GrayText.copy(alpha = 0.35f),
                        ),
                )
            }
        }
    }
}

@Composable
private fun InformationImage(
    path: String,
    modifier: Modifier = Modifier,
) {
    val image = rememberComposeResourceImage(path)

    if (image != null) {
        Image(
            bitmap = image,
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = modifier.fillMaxSize(),
        )
    }
}

@Composable
@Preview
private fun InformationScreenPreview() {
    HvalaTheme {
        ProvideAppLocalization(AppLanguage.RU) {
            InformationScreen()
        }
    }
}

package tech.appard.hvala.shared.feature.settings.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import tech.appard.hvala.shared.core.i18n.AppLanguage
import tech.appard.hvala.shared.core.i18n.HelpFaqItem
import tech.appard.hvala.shared.core.i18n.ProvideAppLocalization
import tech.appard.hvala.shared.core.i18n.appStrings
import tech.appard.hvala.shared.core.ui.components.buttons.PrimaryButton
import tech.appard.hvala.shared.core.ui.components.fields.PrimaryTextField
import tech.appard.hvala.shared.core.ui.platform.rememberExternalLinkOpener
import tech.appard.hvala.shared.core.ui.theme.BodyMedium
import tech.appard.hvala.shared.core.ui.theme.FieldTitle
import tech.appard.hvala.shared.core.ui.theme.GrayText
import tech.appard.hvala.shared.core.ui.theme.HvalaTheme
import tech.appard.hvala.shared.core.ui.theme.InputText
import tech.appard.hvala.shared.core.ui.theme.LocalDimensions
import tech.appard.hvala.shared.core.ui.theme.ScreenBackground
import tech.appard.hvala.shared.core.ui.theme.White

private const val SUPPORT_EMAIL = "support@hvala.app"

@Composable
fun HelpScreen(
    modifier: Modifier = Modifier,
) {
    val dimensions = LocalDimensions.current
    val strings = appStrings().help
    val linkOpener = rememberExternalLinkOpener()
    var query by rememberSaveable { mutableStateOf("") }
    val filteredItems = remember(query, strings.faqItems) {
        if (query.isBlank()) {
            strings.faqItems
        } else {
            strings.faqItems.filter { item ->
                item.question.contains(query, ignoreCase = true) ||
                    item.answer.contains(query, ignoreCase = true)
            }
        }
    }

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
            text = strings.searchQuestion,
            style = FieldTitle.copy(color = InputText),
        )

        PrimaryTextField(
            modifier = Modifier.fillMaxWidth(),
            value = query,
            placeholder = strings.enterQuestion,
            isMaxQuantityOfCharVisible = false,
            onTextChange = { query = it },
        )

        filteredItems.forEach { item ->
            HelpFaqCard(item = item)
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(White)
                .padding(dimensions.horizontalMedium),
            verticalArrangement = Arrangement.spacedBy(dimensions.verticalSmall),
        ) {
            Text(
                text = strings.supportTitle,
                style = FieldTitle.copy(color = InputText),
            )
            Text(
                text = strings.supportDescription,
                style = BodyMedium.copy(color = GrayText),
            )
            PrimaryButton(
                text = strings.sendSupport,
                onClick = { linkOpener.openEmail(SUPPORT_EMAIL) },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun HelpFaqCard(
    item: HelpFaqItem,
    modifier: Modifier = Modifier,
) {
    val dimensions = LocalDimensions.current
    var expanded by rememberSaveable(item.question) { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(White)
            .clickable { expanded = !expanded }
            .padding(dimensions.horizontalMedium),
        verticalArrangement = Arrangement.spacedBy(dimensions.verticalSmall),
    ) {
        Text(
            text = item.question,
            style = FieldTitle.copy(color = InputText),
        )
        AnimatedVisibility(visible = expanded) {
            Text(
                text = item.answer,
                style = BodyMedium.copy(color = GrayText),
                modifier = Modifier.padding(top = dimensions.verticalXSmall),
            )
        }
    }
}

@Composable
@Preview
private fun HelpScreenPreview() {
    HvalaTheme {
        ProvideAppLocalization(AppLanguage.RU) {
            HelpScreen()
        }
    }
}

package tech.appard.hvala.shared.feature.profile.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import tech.appard.hvala.shared.core.i18n.appStrings
import tech.appard.hvala.shared.core.ui.components.buttons.PrimaryButton
import tech.appard.hvala.shared.core.ui.components.fields.PhoneTextField
import tech.appard.hvala.shared.core.ui.components.fields.PrimaryTextField
import tech.appard.hvala.shared.core.ui.theme.BodyMedium
import tech.appard.hvala.shared.core.ui.theme.ButtonLarge
import tech.appard.hvala.shared.core.ui.theme.Error
import tech.appard.hvala.shared.core.ui.theme.FieldTitle
import tech.appard.hvala.shared.core.ui.theme.HvalaTheme
import tech.appard.hvala.shared.core.ui.theme.InputText
import tech.appard.hvala.shared.core.ui.theme.LocalDimensions
import tech.appard.hvala.shared.core.ui.theme.ScreenBackground
import tech.appard.hvala.shared.core.ui.theme.SecondaryMain
import tech.appard.hvala.shared.feature.profile.presentation.viewmodels.EditProfileEffect
import tech.appard.hvala.shared.feature.profile.presentation.viewmodels.EditProfileStateHolder
import tech.appard.hvala.shared.feature.profile.presentation.viewmodels.EditProfileUiState

@Composable
fun EditProfileScreen(
    stateHolder: EditProfileStateHolder,
    modifier: Modifier = Modifier,
    onSaved: () -> Unit = {},
) {
    val state by stateHolder.state.collectAsState()

    LaunchedEffect(stateHolder) {
        stateHolder.load()
    }

    LaunchedEffect(stateHolder) {
        stateHolder.effects.collect { effect ->
            when (effect) {
                EditProfileEffect.Saved -> onSaved()
            }
        }
    }

    EditProfileContent(
        modifier = modifier,
        state = state,
        onFullNameChange = stateHolder::onFullNameChange,
        onEmailChange = stateHolder::onEmailChange,
        onPhoneChange = stateHolder::onPhoneChange,
        onSaveClick = stateHolder::save,
    )
}

@Composable
private fun EditProfileContent(
    state: EditProfileUiState,
    onFullNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimensions = LocalDimensions.current
    val authStrings = appStrings().auth
    val settingsStrings = appStrings().settings
    val fieldModifier = Modifier
        .fillMaxWidth()
        .height(dimensions.fieldsDefaultHeight)

    BoxWithLoading(
        modifier = modifier
            .fillMaxSize()
            .background(ScreenBackground),
        isLoading = state.isLoading,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = dimensions.horizontalMedium)
                .padding(bottom = dimensions.verticalLarge)
                .imePadding(),
            verticalArrangement = Arrangement.spacedBy(dimensions.verticalMedium),
        ) {
            Spacer(modifier = Modifier.height(dimensions.verticalMedium))

            EditProfileField(label = authStrings.fullNameLabel) {
                PrimaryTextField(
                    modifier = fieldModifier,
                    value = state.fullName,
                    placeholder = authStrings.fullNamePlaceholder,
                    onTextChange = onFullNameChange,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next,
                    ),
                )
            }

            EditProfileField(label = authStrings.emailLabel) {
                PrimaryTextField(
                    modifier = fieldModifier,
                    value = state.email,
                    placeholder = authStrings.email,
                    onTextChange = onEmailChange,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next,
                    ),
                )
            }

            EditProfileField(label = authStrings.phoneLabel) {
                PhoneTextField(
                    modifier = fieldModifier,
                    value = state.phone,
                    placeholder = authStrings.phonePlaceholder,
                    onTextChange = onPhoneChange,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Done,
                    ),
                )
            }

            state.error?.let { error ->
                Text(
                    text = error,
                    style = BodyMedium.copy(color = Error),
                )
            }

            PrimaryButton(
                text = settingsStrings.saveChanges,
                onClick = onSaveClick,
                modifier = Modifier.fillMaxWidth(),
                textStyle = ButtonLarge,
                enabled = !state.isSaving,
            )
        }
    }
}

@Composable
private fun BoxWithLoading(
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    if (isLoading) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator(color = SecondaryMain)
        }
    } else {
        content()
    }
}

@Composable
private fun EditProfileField(
    label: String,
    content: @Composable () -> Unit,
) {
    val dimensions = LocalDimensions.current

    Column(verticalArrangement = Arrangement.spacedBy(dimensions.verticalXXSmall)) {
        Text(
            text = label,
            style = FieldTitle.copy(color = InputText),
        )
        content()
    }
}

@Composable
@Preview
private fun EditProfileScreenPreview() {
    HvalaTheme {
        EditProfileContent(
            state = EditProfileUiState(
                profileId = "1",
                fullName = "Vadim",
                email = "vadim@example.com",
                phone = "+382 67 123 456",
                isLoading = false,
            ),
            onFullNameChange = {},
            onEmailChange = {},
            onPhoneChange = {},
            onSaveClick = {},
        )
    }
}

package tech.appard.hvala.shared.feature.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import tech.appard.hvala.shared.core.ui.components.buttons.PrimaryButton
import tech.appard.hvala.shared.core.ui.components.fields.PasswordTextField
import tech.appard.hvala.shared.core.ui.components.fields.PhoneTextField
import tech.appard.hvala.shared.core.ui.components.fields.PrimaryTextField
import tech.appard.hvala.shared.core.ui.theme.BodyMedium
import tech.appard.hvala.shared.core.ui.theme.ButtonLarge
import tech.appard.hvala.shared.core.ui.theme.Error
import tech.appard.hvala.shared.core.ui.theme.FieldCaption
import tech.appard.hvala.shared.core.ui.theme.FieldTitle
import tech.appard.hvala.shared.core.ui.theme.GrayText
import tech.appard.hvala.shared.core.ui.theme.HvalaTheme
import tech.appard.hvala.shared.core.ui.theme.InputText
import tech.appard.hvala.shared.core.ui.theme.LocalDimensions
import tech.appard.hvala.shared.core.ui.theme.SecondaryMain
import tech.appard.hvala.shared.core.ui.theme.White

@Composable
fun RegistrationScreen(
    stateHolder: AuthStateHolder,
    modifier: Modifier = Modifier,
    onRegistered: () -> Unit = {},
) {
    val state by stateHolder.registrationState.collectAsState()

    RegistrationContent(
        modifier = modifier,
        state = state,
        onFullNameChange = stateHolder::onRegistrationFullNameChange,
        onEmailChange = stateHolder::onRegistrationEmailChange,
        onPhoneChange = stateHolder::onRegistrationPhoneChange,
        onPasswordChange = stateHolder::onRegistrationPasswordChange,
        onConfirmPasswordChange = stateHolder::onRegistrationConfirmPasswordChange,
        onTermsAcceptedChange = stateHolder::onRegistrationTermsAcceptedChange,
        onSignUp = { stateHolder.signUp(onRegistered) },
    )
}

@Composable
private fun RegistrationContent(
    state: RegistrationUiState,
    onFullNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onTermsAcceptedChange: (Boolean) -> Unit,
    onSignUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimensions = LocalDimensions.current
    val fieldModifier = Modifier
        .fillMaxWidth()
        .height(dimensions.fieldsDefaultHeight)

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = dimensions.horizontalMedium)
            .imePadding(),
        verticalArrangement = Arrangement.spacedBy(dimensions.verticalMedium),
    ) {
        Spacer(modifier = Modifier.height(dimensions.verticalMedium))

        RegistrationField(label = "Как вас зовут?") {
            PrimaryTextField(
                modifier = fieldModifier,
                value = state.fullName,
                placeholder = "Иван Иванов",
                isMaxQuantityOfCharVisible = false,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next,
                ),
                onTextChange = onFullNameChange,
            )
        }

        RegistrationField(label = "Ваш email") {
            PrimaryTextField(
                modifier = fieldModifier,
                value = state.email,
                placeholder = "email@example.com",
                isMaxQuantityOfCharVisible = false,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next,
                ),
                onTextChange = onEmailChange,
            )
        }

        RegistrationField(label = "Ваш телефон") {
            PhoneTextField(
                modifier = fieldModifier,
                value = state.phone,
                placeholder = "+7 (999) 000-00-00",
                isMaxQuantityOfCharVisible = false,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Next,
                ),
                onTextChange = onPhoneChange,
            )
        }

        RegistrationField(label = "Придумайте пароль") {
            PasswordTextField(
                modifier = fieldModifier,
                value = state.password,
                placeholder = "Password",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next,
                ),
                onTextChange = onPasswordChange,
            )
        }

        RegistrationField(label = "Повторите пароль") {
            PasswordTextField(
                modifier = fieldModifier,
                value = state.confirmPassword,
                placeholder = "Password",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done,
                ),
                onTextChange = onConfirmPasswordChange,
            )
        }

        if (state.error != null) {
            Text(
                text = state.error,
                style = FieldCaption.copy(color = Error),
                modifier = Modifier.fillMaxWidth(),
            )
        }

        PrimaryButton(
            text = "Sign up",
            onClick = onSignUp,
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading,
            loading = state.isLoading,
            textStyle = ButtonLarge,
        )

        RegistrationTermsRow(
            checked = state.isTermsAccepted,
            onCheckedChange = onTermsAcceptedChange,
            modifier = Modifier.padding(bottom = dimensions.verticalXLarge),
        )
    }
}

@Composable
private fun RegistrationField(
    label: String,
    modifier: Modifier = Modifier,
    field: @Composable () -> Unit,
) {
    val dimensions = LocalDimensions.current

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(dimensions.verticalXXSmall),
    ) {
        Text(
            text = label,
            style = FieldTitle.copy(color = InputText),
        )
        field()
    }
}

@Composable
private fun RegistrationTermsRow(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { onCheckedChange(!checked) },
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(LocalDimensions.current.horizontalXSmall),
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = SecondaryMain,
                uncheckedColor = GrayText,
                checkmarkColor = White,
            ),
        )
        Text(
            text = "Я принимаю пользовательское соглашение Hvala",
            style = BodyMedium.copy(color = GrayText),
        )
    }
}

@Composable
@Preview
private fun RegistrationScreenPreview() {
    HvalaTheme {
        RegistrationContent(
            state = RegistrationUiState(
                isTermsAccepted = true,
            ),
            onFullNameChange = {},
            onEmailChange = {},
            onPhoneChange = {},
            onPasswordChange = {},
            onConfirmPasswordChange = {},
            onTermsAcceptedChange = {},
            onSignUp = {},
        )
    }
}

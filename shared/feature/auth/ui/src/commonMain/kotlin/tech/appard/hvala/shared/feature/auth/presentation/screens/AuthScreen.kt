package tech.appard.hvala.shared.feature.auth.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import tech.appard.hvala.shared.core.ui.components.buttons.PrimaryButton
import tech.appard.hvala.shared.core.ui.components.fields.PasswordTextField
import tech.appard.hvala.shared.core.ui.components.fields.PrimaryTextField
import tech.appard.hvala.shared.core.ui.components.logo.HvalaLogo
import tech.appard.hvala.shared.core.ui.theme.Error
import tech.appard.hvala.shared.core.ui.theme.BodyMedium
import tech.appard.hvala.shared.core.ui.theme.ButtonLarge
import tech.appard.hvala.shared.core.ui.theme.FieldCaption
import tech.appard.hvala.shared.core.ui.theme.GrayText
import tech.appard.hvala.shared.core.ui.theme.HvalaTheme
import tech.appard.hvala.shared.core.ui.theme.LinkMedium
import tech.appard.hvala.shared.core.ui.theme.LocalDimensions
import tech.appard.hvala.shared.core.ui.theme.SecondaryMain
import tech.appard.hvala.shared.core.i18n.appStrings
import tech.appard.hvala.shared.feature.auth.presentation.AuthStateHolder

@Composable
fun AuthScreen(
    stateHolder: AuthStateHolder,
    modifier: Modifier = Modifier,
    onAuthenticated: () -> Unit = {},
    onForgotPasswordClick: () -> Unit = {},
    onSignUpClick: () -> Unit = {},
) {
    val state by stateHolder.state.collectAsState()

    AuthContent(
        modifier = modifier,
        login = state.login,
        password = state.password,
        error = state.error,
        isLoading = state.isLoading,
        onLoginChange = stateHolder::onLoginChange,
        onPasswordChange = stateHolder::onPasswordChange,
        onSignIn = { stateHolder.signIn(onAuthenticated) },
        onForgotPasswordClick = onForgotPasswordClick,
        onSignUpClick = onSignUpClick,
    )
}

@Composable
private fun AuthContent(
    modifier: Modifier = Modifier,
    login: String,
    password: String,
    error: String?,
    isLoading: Boolean,
    onLoginChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSignIn: () -> Unit,
    onForgotPasswordClick: () -> Unit = {},
    onSignUpClick: () -> Unit = {},
) {
    val dimensions = LocalDimensions.current
    val strings = appStrings().auth

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = dimensions.horizontalMedium)
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            AuthFormBlock(
                login = login,
                password = password,
                error = error,
                isLoading = isLoading,
                onLoginChange = onLoginChange,
                onPasswordChange = onPasswordChange,
                onSignIn = onSignIn,
                onForgotPasswordClick = onForgotPasswordClick,
            )
        }

        Row(
            modifier = Modifier.padding(bottom = dimensions.verticalXLarge),
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                text = strings.noAccount,
                style = BodyMedium.copy(color = GrayText),
            )
            Text(
                text = strings.signUp,
                style = LinkMedium.copy(color = SecondaryMain),
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onSignUpClick,
                ),
            )
        }
    }
}

@Composable
private fun AuthFormBlock(
    login: String,
    password: String,
    error: String?,
    isLoading: Boolean,
    onLoginChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSignIn: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimensions = LocalDimensions.current
    val strings = appStrings().auth

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensions.verticalMedium),
    ) {
        HvalaLogo()

        AuthEmailField(
            value = login,
            onValueChange = onLoginChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensions.fieldsDefaultHeight),
        )

        AuthPasswordField(
            value = password,
            onValueChange = onPasswordChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensions.fieldsDefaultHeight),
        )

        AuthForgotPasswordAndErrorRow(
            error = error,
            onForgotPasswordClick = onForgotPasswordClick,
        )

        AuthLoginButton(
            isLoading = isLoading,
            onClick = onSignIn,
            loginText = strings.login,
        )
    }
}

@Composable
private fun AuthEmailField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val strings = appStrings().auth
    PrimaryTextField(
        modifier = modifier,
        value = value,
        placeholder = strings.email,
        isMaxQuantityOfCharVisible = false,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next,
        ),
        onTextChange = onValueChange,
    )
}

@Composable
private fun AuthPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val strings = appStrings().auth
    PasswordTextField(
        modifier = modifier,
        value = value,
        placeholder = strings.password,
        onTextChange = onValueChange,
    )
}

@Composable
private fun AuthForgotPasswordAndErrorRow(
    error: String?,
    onForgotPasswordClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimensions = LocalDimensions.current
    val strings = appStrings().auth

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(dimensions.verticalLarge),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        if (error != null) {
            Text(
                text = error,
                style = FieldCaption.copy(color = Error),
                modifier = Modifier.weight(1f),
                maxLines = 1,
                textAlign = TextAlign.Start,
            )
        } else {
            Spacer(modifier = Modifier.weight(1f))
        }

        Text(
            text = strings.forgotPassword,
            style = LinkMedium.copy(color = SecondaryMain),
            modifier = Modifier.clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onForgotPasswordClick,
            ),
        )
    }
}

@Composable
private fun AuthLoginButton(
    isLoading: Boolean,
    onClick: () -> Unit,
    loginText: String,
    modifier: Modifier = Modifier,
) {
    PrimaryButton(
        text = loginText,
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        enabled = !isLoading,
        loading = isLoading,
        textStyle = ButtonLarge,
    )
}

@Composable
@Preview
private fun AuthScreenPreview() {
    HvalaTheme {
        AuthContent(
            login = "",
            password = "",
            error = null,
            isLoading = false,
            onLoginChange = {},
            onPasswordChange = {},
            onSignIn = {},
        )
    }
}

@Composable
@Preview
private fun AuthScreenErrorPreview() {
    HvalaTheme {
        AuthContent(
            login = "user@example.com",
            password = "secret",
            error = "Invalid credentials",
            isLoading = false,
            onLoginChange = {},
            onPasswordChange = {},
            onSignIn = {},
        )
    }
}

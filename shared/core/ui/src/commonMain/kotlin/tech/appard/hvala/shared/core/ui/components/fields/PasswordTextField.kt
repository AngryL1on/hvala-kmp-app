package tech.appard.hvala.shared.core.ui.components.fields

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import tech.appard.hvala.shared.core.ui.icons.PasswordVisibilityIcon
import tech.appard.hvala.shared.core.ui.theme.GrayPlaceholder
import tech.appard.hvala.shared.core.ui.theme.LocalDimensions

@Composable
fun PasswordTextField(
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    value: String = "",
    placeholder: String? = null,
    supportText: String? = null,
    errorText: String? = null,
    isEnabled: Boolean = true,
    isError: Boolean = false,
    passwordVisible: Boolean = false,
    onPasswordVisibilityToggle: () -> Unit = {},
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Password,
        imeAction = ImeAction.Done,
    ),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val dimensions = LocalDimensions.current

    PrimaryTextField(
        modifier = modifier,
        title = title,
        value = value,
        placeholder = placeholder,
        supportText = supportText,
        errorText = errorText,
        isEnabled = isEnabled,
        isError = isError,
        isMaxQuantityOfCharVisible = false,
        visualTransformation = if (passwordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        keyboardOptions = keyboardOptions,
        onTextChange = onTextChange,
        interactionSource = interactionSource,
        trailingContent = {
            IconButton(
                modifier = Modifier.size(dimensions.iconDefaultSize),
                onClick = onPasswordVisibilityToggle,
            ) {
                PasswordVisibilityIcon(
                    visible = passwordVisible,
                    tint = GrayPlaceholder,
                )
            }
        },
    )
}

@Composable
fun PasswordTextField(
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    value: String = "",
    placeholder: String? = null,
    supportText: String? = null,
    errorText: String? = null,
    isEnabled: Boolean = true,
    isError: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Password,
        imeAction = ImeAction.Done,
    ),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    var passwordVisible by remember { mutableStateOf(false) }

    PasswordTextField(
        modifier = modifier,
        title = title,
        value = value,
        placeholder = placeholder,
        supportText = supportText,
        errorText = errorText,
        isEnabled = isEnabled,
        isError = isError,
        passwordVisible = passwordVisible,
        onPasswordVisibilityToggle = { passwordVisible = !passwordVisible },
        keyboardOptions = keyboardOptions,
        onTextChange = onTextChange,
        interactionSource = interactionSource,
    )
}

package tech.appard.hvala.shared.core.ui.components.fields

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import tech.appard.hvala.shared.core.ui.utils.PhoneVisualTransformation

@Composable
fun PhoneTextField(
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    value: String = "",
    placeholder: String? = null,
    supportText: String? = null,
    errorText: String? = null,
    isEnabled: Boolean = true,
    isError: Boolean = false,
    maxQuantityOfChar: Int = 11,
    isMaxQuantityOfCharVisible: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Phone,
        imeAction = ImeAction.Done,
    ),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    PrimaryTextField(
        modifier = modifier,
        title = title,
        value = value,
        placeholder = placeholder,
        supportText = supportText,
        errorText = errorText,
        isEnabled = isEnabled,
        isError = isError,
        isOnlyNumbers = true,
        maxQuantityOfChar = maxQuantityOfChar,
        isMaxQuantityOfCharVisible = isMaxQuantityOfCharVisible,
        visualTransformation = PhoneVisualTransformation,
        keyboardOptions = keyboardOptions,
        onTextChange = onTextChange,
        interactionSource = interactionSource,
    )
}

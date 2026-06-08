package tech.appard.hvala.shared.core.ui.components.fields

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.appard.hvala.shared.core.ui.theme.Error
import tech.appard.hvala.shared.core.ui.theme.FieldCaption
import tech.appard.hvala.shared.core.ui.theme.FieldInput
import tech.appard.hvala.shared.core.ui.theme.FieldTitle
import tech.appard.hvala.shared.core.ui.theme.GrayPlaceholder
import tech.appard.hvala.shared.core.ui.theme.HvalaTheme
import tech.appard.hvala.shared.core.ui.theme.InputBackground
import tech.appard.hvala.shared.core.ui.theme.InputBorder
import tech.appard.hvala.shared.core.ui.theme.InputText
import tech.appard.hvala.shared.core.ui.theme.LocalDimensions
import tech.appard.hvala.shared.core.ui.theme.SecondaryMain
import tech.appard.hvala.shared.core.ui.theme.White

@Composable
fun PrimaryTextField(
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    value: String = "",
    prefix: String? = null,
    placeholder: String? = null,
    supportText: String? = null,
    errorText: String? = null,
    maxQuantityOfChar: Int? = null,
    isMaxQuantityOfCharVisible: Boolean = true,
    maxLines: Int = 1,
    minLines: Int = 1,
    singleLine: Boolean = true,
    readOnly: Boolean = false,
    isEnabled: Boolean = true,
    isError: Boolean = false,
    isOnlyNumbers: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    trailingContent: @Composable (() -> Unit)? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val dimensions = LocalDimensions.current
    val fillMaxWidthModifier = Modifier.fillMaxWidth()
    val finalKeyboardOptions = if (isOnlyNumbers) {
        keyboardOptions.copy(keyboardType = KeyboardType.Number)
    } else {
        keyboardOptions
    }
    val borderColor = when {
        isError -> Error
        !isEnabled -> InputBorder.copy(alpha = 0.5f)
        else -> InputBorder
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensions.verticalXXSmall),
    ) {
        title?.let {
            Text(
                text = title,
                style = FieldTitle.copy(color = InputText),
            )
        }

        BasicTextField(
            modifier = fillMaxWidthModifier,
            value = value,
            onValueChange = { text ->
                val filteredText = if (isOnlyNumbers) {
                    text.filter(Char::isDigit)
                } else {
                    text
                }
                val maxChars = maxQuantityOfChar ?: Int.MAX_VALUE
                if (filteredText.length <= maxChars) {
                    onTextChange(filteredText)
                }
            },
            visualTransformation = visualTransformation,
            textStyle = FieldInput.copy(color = InputText),
            readOnly = readOnly,
            enabled = isEnabled,
            maxLines = maxLines,
            minLines = minLines,
            singleLine = singleLine,
            keyboardOptions = finalKeyboardOptions,
            interactionSource = interactionSource,
            cursorBrush = SolidColor(SecondaryMain),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(dimensions.defaultCornerRadius))
                        .border(
                            width = 1.dp,
                            color = borderColor,
                            shape = RoundedCornerShape(dimensions.defaultCornerRadius),
                        )
                        .background(InputBackground)
                        .padding(
                            vertical = dimensions.verticalMedium,
                            horizontal = dimensions.horizontalMedium,
                        )
                        .requiredHeightIn(min = dimensions.verticalHuge),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    prefix?.let { prefixText ->
                        Text(
                            text = "$prefixText ",
                            style = FieldInput.copy(color = InputText),
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = dimensions.horizontalXSmall),
                    ) {
                        if (value.isEmpty() && placeholder != null) {
                            Text(
                                text = placeholder,
                                style = FieldInput.copy(color = GrayPlaceholder),
                            )
                        }
                        innerTextField()
                    }

                    trailingContent?.invoke()
                }
            },
        )

        if (isError || supportText != null || (maxQuantityOfChar != null && isMaxQuantityOfCharVisible)) {
            Row(
                modifier = fillMaxWidthModifier.padding(top = dimensions.verticalXXSmall),
                verticalAlignment = Alignment.Top,
            ) {
                when {
                    isError && errorText != null -> {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = errorText,
                            style = FieldCaption,
                            color = Error,
                            textAlign = TextAlign.Start,
                        )
                    }
                    supportText != null -> {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = supportText,
                            style = FieldCaption,
                            color = InputText,
                            textAlign = TextAlign.Start,
                        )
                    }
                    else -> {
                        Box(modifier = Modifier.weight(1f))
                    }
                }

                if (maxQuantityOfChar != null && isMaxQuantityOfCharVisible) {
                    Text(
                        text = "${value.length}/$maxQuantityOfChar",
                        style = FieldCaption,
                        color = InputText,
                        textAlign = TextAlign.End,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PrimaryTextFieldPreview() {
    HvalaTheme {
        val dimensions = LocalDimensions.current

        Surface(color = White) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(dimensions.defaultPadding),
                verticalArrangement = Arrangement.spacedBy(dimensions.verticalMedium),
            ) {
                PrimaryTextField(
                    title = "Имя пользователя",
                    placeholder = "Введите имя",
                    maxQuantityOfChar = 20,
                    isMaxQuantityOfCharVisible = false,
                    onTextChange = {},
                )

                PrimaryTextField(
                    title = "Электронная почта",
                    placeholder = "Введите электронную почту",
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Done,
                    ),
                    isMaxQuantityOfCharVisible = false,
                    onTextChange = {},
                )

                PrimaryTextField(
                    title = "Комментарий",
                    placeholder = "Не более 500 символов",
                    supportText = "Только латиница",
                    minLines = 4,
                    maxLines = 4,
                    singleLine = false,
                    maxQuantityOfChar = 500,
                    onTextChange = {},
                )

                PrimaryTextField(
                    title = "Имя пользователя",
                    placeholder = "Введите имя",
                    maxQuantityOfChar = 20,
                    isMaxQuantityOfCharVisible = true,
                    isError = true,
                    errorText = "Данный пользователь не найден",
                    onTextChange = {},
                )
            }
        }
    }
}

package tech.appard.hvala.shared.core.ui.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

object PhoneVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val digits = text.text.filter(Char::isDigit).take(11)
        val formatted = buildString {
            digits.forEachIndexed { index, char ->
                when (index) {
                    0 -> append("+7 (")
                    3 -> append(") ")
                    6 -> append("-")
                    8 -> append("-")
                }
                append(char)
            }
        }

        return TransformedText(
            text = AnnotatedString(formatted),
            offsetMapping = PhoneOffsetMapping(digits.length),
        )
    }
}

private class PhoneOffsetMapping(
    private val digitsLength: Int,
) : OffsetMapping {
    override fun originalToTransformed(offset: Int): Int {
        val safeOffset = offset.coerceIn(0, digitsLength)
        return when {
            safeOffset <= 0 -> 0
            safeOffset <= 1 -> safeOffset + 4
            safeOffset <= 4 -> safeOffset + 4
            safeOffset <= 7 -> safeOffset + 6
            safeOffset <= 9 -> safeOffset + 7
            else -> safeOffset + 8
        }.coerceAtMost(formattedLength(digitsLength))
    }

    override fun transformedToOriginal(offset: Int): Int {
        val formatted = formattedLength(digitsLength)
        val safeOffset = offset.coerceIn(0, formatted)
        return when {
            safeOffset <= 4 -> (safeOffset - 4).coerceAtLeast(0)
            safeOffset <= 5 -> 1
            safeOffset <= 9 -> safeOffset - 5
            safeOffset <= 10 -> 4
            safeOffset <= 13 -> safeOffset - 6
            safeOffset <= 14 -> 7
            safeOffset <= 16 -> safeOffset - 7
            safeOffset <= 17 -> 9
            else -> digitsLength
        }.coerceIn(0, digitsLength)
    }

    private fun formattedLength(digits: Int): Int = when {
        digits <= 0 -> 0
        digits == 1 -> 5
        digits <= 4 -> digits + 4
        digits <= 7 -> digits + 6
        digits <= 9 -> digits + 7
        else -> digits + 8
    }
}

package tech.appard.hvala.shared.core.ui.components.fields

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import tech.appard.hvala.shared.core.ui.theme.FieldInput
import tech.appard.hvala.shared.core.ui.theme.FieldTitle
import tech.appard.hvala.shared.core.ui.theme.GrayPlaceholder
import tech.appard.hvala.shared.core.ui.theme.GrayText
import tech.appard.hvala.shared.core.ui.theme.InputBackground
import tech.appard.hvala.shared.core.ui.theme.InputBorder
import tech.appard.hvala.shared.core.ui.theme.InputText
import tech.appard.hvala.shared.core.ui.theme.LocalDimensions

data class SelectOption(
    val id: String,
    val label: String,
)

@Composable
fun HvalaSelectField(
    label: String,
    options: List<SelectOption>,
    selectedOptionId: String?,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    enabled: Boolean = true,
    fieldMinHeight: Dp? = null,
) {
    val dimensions = LocalDimensions.current
    var expanded by remember { mutableStateOf(false) }
    val selectedLabel = options.firstOrNull { it.id == selectedOptionId }?.label.orEmpty()
    val displayText = selectedLabel.ifBlank { placeholder }
    val displayColor = if (selectedLabel.isBlank()) GrayPlaceholder else InputText
    val shape = RoundedCornerShape(dimensions.defaultCornerRadius)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensions.verticalXXSmall),
    ) {
        Text(
            text = label,
            style = FieldTitle.copy(color = GrayText),
        )

        Box {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape)
                    .border(width = 1.dp, color = InputBorder, shape = shape)
                    .background(InputBackground)
                    .requiredHeightIn(min = fieldMinHeight ?: dimensions.fieldsDefaultHeight)
                    .clickable(
                        enabled = enabled && options.isNotEmpty(),
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { expanded = true },
                    )
                    .padding(horizontal = dimensions.horizontalMedium),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = displayText,
                    style = FieldInput.copy(color = displayColor),
                    modifier = Modifier.weight(1f),
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = GrayText,
                    modifier = Modifier.size(dimensions.iconDefaultSize),
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option.label) },
                        onClick = {
                            onOptionSelected(option.id)
                            expanded = false
                        },
                    )
                }
            }
        }
    }
}

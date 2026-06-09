package tech.appard.hvala.shared.feature.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import tech.appard.hvala.shared.core.ui.components.image.LocalUriImage
import tech.appard.hvala.shared.core.ui.theme.CardBorder
import tech.appard.hvala.shared.core.ui.theme.FieldTitle
import tech.appard.hvala.shared.core.ui.theme.GrayPlaceholder
import tech.appard.hvala.shared.core.ui.theme.InputText
import tech.appard.hvala.shared.core.ui.theme.LocalDimensions
import tech.appard.hvala.shared.core.ui.theme.ScreenBackground

@Composable
fun ReviewPhotoPickerSection(
    label: String,
    photoUris: List<String>,
    onAddPhotoClick: () -> Unit,
    modifier: Modifier = Modifier,
    canAddMore: Boolean = true,
    addPhotoDescription: String? = null,
) {
    val dimensions = LocalDimensions.current
    val photoSize = dimensions.settingsAvatarSize - 16.dp
    val shape = RoundedCornerShape(dimensions.defaultCornerRadius)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensions.verticalXSmall),
    ) {
        Text(
            text = label,
            style = FieldTitle.copy(color = InputText),
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(dimensions.horizontalXSmall),
        ) {
            if (canAddMore) {
                item(key = "add-photo") {
                    Box(
                        modifier = Modifier
                            .size(photoSize)
                            .clip(shape)
                            .background(ScreenBackground)
                            .border(width = 1.dp, color = CardBorder, shape = shape)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = onAddPhotoClick,
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = addPhotoDescription,
                            tint = GrayPlaceholder,
                            modifier = Modifier.size(dimensions.iconDefaultSize),
                        )
                    }
                }
            }

            items(photoUris, key = { it }) { uri ->
                Box(
                    modifier = Modifier
                        .size(photoSize)
                        .clip(shape)
                        .background(ScreenBackground)
                        .border(width = 1.dp, color = CardBorder, shape = shape),
                ) {
                    LocalUriImage(
                        uri = uri,
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }
        }
    }
}

@Composable
fun ReviewPhotoGallery(
    photoUris: List<String>,
    modifier: Modifier = Modifier,
) {
    if (photoUris.isEmpty()) return

    val dimensions = LocalDimensions.current
    val photoSize = dimensions.settingsAvatarSize
    val shape = RoundedCornerShape(dimensions.defaultCornerRadius)

    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(dimensions.horizontalXSmall),
    ) {
        items(photoUris, key = { it }) { uri ->
            Box(
                modifier = Modifier
                    .size(photoSize)
                    .clip(shape)
                    .background(ScreenBackground)
                    .border(width = 1.dp, color = CardBorder, shape = shape),
            ) {
                LocalUriImage(
                    uri = uri,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

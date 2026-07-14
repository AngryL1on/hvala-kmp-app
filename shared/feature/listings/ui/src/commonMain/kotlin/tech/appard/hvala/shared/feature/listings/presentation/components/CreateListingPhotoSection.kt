package tech.appard.hvala.shared.feature.listings.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import tech.appard.hvala.shared.core.i18n.appStrings
import tech.appard.hvala.shared.core.ui.model.PickedMedia
import tech.appard.hvala.shared.core.ui.platform.PickedMediaImage
import tech.appard.hvala.shared.core.ui.theme.CardBorder
import tech.appard.hvala.shared.core.ui.theme.FieldTitle
import tech.appard.hvala.shared.core.ui.theme.GrayPlaceholder
import tech.appard.hvala.shared.core.ui.theme.InputText
import tech.appard.hvala.shared.core.ui.theme.LocalDimensions
import tech.appard.hvala.shared.core.ui.theme.ScreenBackground
import tech.appard.hvala.shared.core.ui.theme.White

@Composable
fun CreateListingPhotoSection(
    photos: List<PickedMedia>,
    onAddPhotoClick: () -> Unit,
    modifier: Modifier = Modifier,
    canAddMore: Boolean = true,
    onPhotoRemove: (String) -> Unit = {},
) {
    val dimensions = LocalDimensions.current
    val strings = appStrings().listings
    val photoSize = dimensions.settingsAvatarSize
    val shape = RoundedCornerShape(dimensions.defaultCornerRadius)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensions.verticalSmall),
    ) {
        Text(
            text = strings.selectPhotos,
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
                            contentDescription = strings.addPhoto,
                            tint = GrayPlaceholder,
                            modifier = Modifier.size(dimensions.iconDefaultSize),
                        )
                    }
                }
            }

            items(photos, key = { it.uri }) { photo ->
                Box(
                    modifier = Modifier
                        .size(photoSize)
                        .clip(shape)
                        .background(ScreenBackground)
                        .border(width = 1.dp, color = CardBorder, shape = shape),
                ) {
                    PickedMediaImage(
                        media = photo,
                        modifier = Modifier.fillMaxSize(),
                        contentDescription = photo.name,
                    )

                    IconButton(
                        onClick = { onPhotoRemove(photo.uri) },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(2.dp)
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(White.copy(alpha = 0.92f)),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = strings.deletePhoto,
                            tint = GrayPlaceholder,
                            modifier = Modifier.size(16.dp),
                        )
                    }
                }
            }
        }
    }
}

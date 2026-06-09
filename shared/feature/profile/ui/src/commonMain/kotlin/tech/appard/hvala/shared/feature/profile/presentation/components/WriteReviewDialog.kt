package tech.appard.hvala.shared.feature.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import tech.appard.hvala.shared.core.ui.components.buttons.PrimaryButton
import tech.appard.hvala.shared.core.ui.components.fields.HvalaSelectField
import tech.appard.hvala.shared.core.ui.components.fields.PrimaryTextField
import tech.appard.hvala.shared.core.ui.components.fields.SelectOption
import tech.appard.hvala.shared.core.ui.theme.BodyMedium
import tech.appard.hvala.shared.core.ui.theme.Error
import tech.appard.hvala.shared.core.ui.theme.FieldTitle
import tech.appard.hvala.shared.core.ui.theme.GrayText
import tech.appard.hvala.shared.core.ui.theme.InputText
import tech.appard.hvala.shared.core.ui.theme.LinkMedium
import tech.appard.hvala.shared.core.ui.theme.LocalDimensions
import tech.appard.hvala.shared.core.ui.theme.ScreenBackground

@Composable
fun WriteReviewDialog(
    title: String,
    ratingLabel: String,
    rating: Int,
    onRatingChange: (Int) -> Unit,
    reviewTextLabel: String,
    reviewText: String,
    onReviewTextChange: (String) -> Unit,
    reviewTextPlaceholder: String,
    photosLabel: String,
    photoUris: List<String>,
    onAddPhotoClick: () -> Unit,
    canAddMorePhotos: Boolean,
    addPhotoDescription: String,
    listingLabel: String,
    listingOptions: List<SelectOption>,
    selectedListingId: String?,
    onListingSelected: (String) -> Unit,
    listingPlaceholder: String,
    cancelText: String,
    submitText: String,
    errorText: String?,
    isSubmitting: Boolean,
    onSubmit: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimensions = LocalDimensions.current

    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = title,
                style = FieldTitle.copy(color = InputText),
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(dimensions.verticalSmall),
            ) {
                Text(
                    text = ratingLabel,
                    style = BodyMedium.copy(color = GrayText),
                )
                InteractiveStarRating(
                    rating = rating,
                    onRatingChange = onRatingChange,
                )

                PrimaryTextField(
                    title = reviewTextLabel,
                    value = reviewText,
                    onTextChange = onReviewTextChange,
                    placeholder = reviewTextPlaceholder,
                    minLines = 3,
                    maxLines = 6,
                )

                ReviewPhotoPickerSection(
                    label = photosLabel,
                    photoUris = photoUris,
                    onAddPhotoClick = onAddPhotoClick,
                    canAddMore = canAddMorePhotos,
                    addPhotoDescription = addPhotoDescription,
                )

                if (listingOptions.isNotEmpty()) {
                    HvalaSelectField(
                        label = listingLabel,
                        options = listingOptions,
                        selectedOptionId = selectedListingId,
                        onOptionSelected = onListingSelected,
                        placeholder = listingPlaceholder,
                    )
                }

                if (errorText != null) {
                    Text(
                        text = errorText,
                        style = BodyMedium.copy(color = Error),
                    )
                }
            }
        },
        confirmButton = {
            PrimaryButton(
                text = submitText,
                onClick = onSubmit,
                enabled = !isSubmitting,
                loading = isSubmitting,
            )
        },
        dismissButton = {
            TextButton(onClick = onDismiss, enabled = !isSubmitting) {
                Text(
                    text = cancelText,
                    style = LinkMedium.copy(color = GrayText),
                )
            }
        },
    )
}

@Composable
fun ReviewSellerReplySection(
    sellerReplyLabel: String,
    authorName: String,
    postedAt: String,
    text: String,
    modifier: Modifier = Modifier,
) {
    val dimensions = LocalDimensions.current
    val shape = RoundedCornerShape(dimensions.defaultCornerRadius)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(ScreenBackground)
            .padding(dimensions.horizontalSmall),
        verticalArrangement = Arrangement.spacedBy(dimensions.verticalXXSmall),
    ) {
        Text(
            text = sellerReplyLabel,
            style = FieldTitle.copy(color = InputText),
        )
        Text(
            text = "$authorName · $postedAt",
            style = BodyMedium.copy(color = GrayText),
        )
        Text(
            text = text,
            style = BodyMedium.copy(color = InputText),
        )
    }
}

package tech.appard.hvala.shared.feature.profile.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import tech.appard.hvala.shared.core.i18n.ReviewStrings
import tech.appard.hvala.shared.core.i18n.appStrings
import tech.appard.hvala.shared.core.ui.components.buttons.PrimaryButton
import tech.appard.hvala.shared.core.ui.components.fields.SelectOption
import tech.appard.hvala.shared.core.ui.model.MediaPickerMode
import tech.appard.hvala.shared.core.ui.platform.rememberMediaPickerLauncher
import tech.appard.hvala.shared.core.ui.theme.BodyMedium
import tech.appard.hvala.shared.core.ui.theme.GrayText
import tech.appard.hvala.shared.core.ui.theme.HvalaTheme
import tech.appard.hvala.shared.core.ui.theme.LocalDimensions
import tech.appard.hvala.shared.core.ui.theme.ScreenBackground
import tech.appard.hvala.shared.core.ui.theme.SecondaryMain
import tech.appard.hvala.shared.core.ui.theme.TitleMedium
import tech.appard.hvala.shared.feature.profile.domain.model.SubmitReviewError
import tech.appard.hvala.shared.feature.profile.domain.model.SubmitReviewReplyError
import tech.appard.hvala.shared.feature.profile.presentation.components.ReplyReviewDialog
import tech.appard.hvala.shared.feature.profile.presentation.components.ReviewCard
import tech.appard.hvala.shared.feature.profile.presentation.components.ReviewListControls
import tech.appard.hvala.shared.feature.profile.presentation.components.ReviewsSummaryCard
import tech.appard.hvala.shared.feature.profile.presentation.components.WriteReviewDialog
import tech.appard.hvala.shared.feature.profile.presentation.model.ReviewSortOrder
import tech.appard.hvala.shared.feature.profile.presentation.model.UIReview
import tech.appard.hvala.shared.feature.profile.presentation.model.UIReviewSummary
import tech.appard.hvala.shared.feature.profile.presentation.viewmodels.ReviewsUiState
import tech.appard.hvala.shared.feature.profile.presentation.viewmodels.ReviewsViewModel

@Composable
fun ReviewsScreen(
    sellerId: String,
    viewModel: ReviewsViewModel,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.state.collectAsState()

    val photoPicker = rememberMediaPickerLauncher(
        mode = MediaPickerMode.Images,
        onResult = { media ->
            viewModel.onWriteReviewPhotosAdded(media.map { it.uri })
        },
    )

    LaunchedEffect(sellerId) {
        viewModel.load(sellerId)
    }

    ReviewsContent(
        modifier = modifier,
        state = state,
        onSortOrderSelected = viewModel::onSortOrderSelected,
        onOnlyWithPhotosToggle = viewModel::onOnlyWithPhotosToggle,
        onLeaveReviewClick = viewModel::onLeaveReviewClick,
        onWriteReviewDismiss = viewModel::onWriteReviewDismiss,
        onWriteReviewRatingChanged = viewModel::onWriteReviewRatingChanged,
        onWriteReviewTextChanged = viewModel::onWriteReviewTextChanged,
        onWriteReviewListingSelected = viewModel::onWriteReviewListingSelected,
        onAddReviewPhotoClick = {
            val dialog = state.writeReviewDialog ?: return@ReviewsContent
            val remaining = ReviewsViewModel.MAX_REVIEW_PHOTOS - dialog.photoUris.size
            if (remaining > 0) {
                photoPicker.launch(maxItems = remaining)
            }
        },
        onSubmitWriteReview = viewModel::onSubmitWriteReview,
        onReplyClick = viewModel::onReplyClick,
        onReplyReviewDismiss = viewModel::onReplyReviewDismiss,
        onReplyReviewTextChanged = viewModel::onReplyReviewTextChanged,
        onSubmitReplyReview = viewModel::onSubmitReplyReview,
    )
}

@Composable
private fun ReviewsContent(
    state: ReviewsUiState,
    onSortOrderSelected: (ReviewSortOrder) -> Unit,
    onOnlyWithPhotosToggle: () -> Unit,
    onLeaveReviewClick: () -> Unit,
    onWriteReviewDismiss: () -> Unit,
    onWriteReviewRatingChanged: (Int) -> Unit,
    onWriteReviewTextChanged: (String) -> Unit,
    onWriteReviewListingSelected: (String) -> Unit,
    onAddReviewPhotoClick: () -> Unit,
    onSubmitWriteReview: () -> Unit,
    onReplyClick: (String) -> Unit,
    onReplyReviewDismiss: () -> Unit,
    onReplyReviewTextChanged: (String) -> Unit,
    onSubmitReplyReview: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimensions = LocalDimensions.current
    val strings = appStrings().reviews

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ScreenBackground),
    ) {
        when {
            state.isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = SecondaryMain,
                )
            }
            state.summary != null -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        top = dimensions.verticalMedium,
                        bottom = dimensions.verticalLarge,
                    ),
                    verticalArrangement = Arrangement.spacedBy(dimensions.verticalMedium),
                ) {
                    item(key = "summary") {
                        ReviewsSummaryCard(
                            summary = state.summary,
                            modifier = Modifier.padding(horizontal = dimensions.horizontalMedium),
                        )
                    }

                    if (state.canLeaveReview) {
                        item(key = "leave-review") {
                            PrimaryButton(
                                text = strings.leaveReview,
                                onClick = onLeaveReviewClick,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = dimensions.horizontalMedium),
                            )
                        }
                    }

                    if (state.summary.totalCount > 0) {
                        item(key = "controls") {
                            ReviewListControls(
                                selectedSortOrder = state.selectedSortOrder,
                                onSortOrderSelected = onSortOrderSelected,
                                onlyWithPhotos = state.onlyWithPhotos,
                                onOnlyWithPhotosToggle = onOnlyWithPhotosToggle,
                            )
                        }

                        if (state.filteredReviews.isEmpty()) {
                            item(key = "empty-filtered") {
                                ReviewsEmptyState(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = dimensions.horizontalLarge),
                                    title = strings.emptyFilteredTitle,
                                    message = strings.emptyFilteredMessage,
                                )
                            }
                        } else {
                            items(
                                items = state.filteredReviews,
                                key = { it.id },
                            ) { review ->
                                ReviewCard(
                                    review = review,
                                    onReplyClick = if (review.canReply) {
                                        { onReplyClick(review.id) }
                                    } else {
                                        null
                                    },
                                    modifier = Modifier.padding(horizontal = dimensions.horizontalMedium),
                                )
                            }
                        }
                    } else if (!state.canLeaveReview) {
                        item(key = "empty") {
                            ReviewsEmptyState(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = dimensions.horizontalLarge),
                                title = strings.emptyTitle,
                                message = strings.emptyMessage,
                            )
                        }
                    }
                }
            }
        }
    }

    state.writeReviewDialog?.let { dialog ->
        WriteReviewDialog(
            title = strings.writeReviewTitle,
            ratingLabel = strings.ratingLabel,
            rating = dialog.rating,
            onRatingChange = onWriteReviewRatingChanged,
            reviewTextLabel = strings.reviewTextLabel,
            reviewText = dialog.text,
            onReviewTextChange = onWriteReviewTextChanged,
            reviewTextPlaceholder = strings.reviewTextPlaceholder,
            photosLabel = strings.photosLabel,
            photoUris = dialog.photoUris,
            onAddPhotoClick = onAddReviewPhotoClick,
            canAddMorePhotos = dialog.photoUris.size < ReviewsViewModel.MAX_REVIEW_PHOTOS,
            addPhotoDescription = strings.addPhoto,
            listingLabel = strings.listingLabel,
            listingOptions = dialog.listingOptions.map { SelectOption(id = it.id, label = it.label) },
            selectedListingId = dialog.selectedListingId,
            onListingSelected = onWriteReviewListingSelected,
            listingPlaceholder = strings.listingPlaceholder,
            cancelText = strings.cancel,
            submitText = strings.submitReview,
            errorText = dialog.error?.let { error -> submitReviewErrorMessage(strings, error) },
            isSubmitting = dialog.isSubmitting,
            onSubmit = onSubmitWriteReview,
            onDismiss = onWriteReviewDismiss,
        )
    }

    state.replyReviewDialog?.let { dialog ->
        ReplyReviewDialog(
            title = strings.replyReviewTitle,
            replyTextLabel = strings.replyTextLabel,
            replyText = dialog.text,
            onReplyTextChange = onReplyReviewTextChanged,
            replyTextPlaceholder = strings.replyTextPlaceholder,
            cancelText = strings.cancel,
            submitText = strings.submitReply,
            errorText = dialog.error?.let { error -> submitReviewReplyErrorMessage(strings, error) },
            isSubmitting = dialog.isSubmitting,
            onSubmit = onSubmitReplyReview,
            onDismiss = onReplyReviewDismiss,
        )
    }
}

private fun submitReviewErrorMessage(
    strings: ReviewStrings,
    error: SubmitReviewError,
): String = when (error) {
    SubmitReviewError.NotAuthenticated -> strings.errorNotAuthenticated
    SubmitReviewError.SelfReview -> strings.errorSelfReview
    SubmitReviewError.AlreadyReviewed -> strings.errorAlreadyReviewed
    SubmitReviewError.InvalidRating -> strings.errorRatingRequired
    SubmitReviewError.EmptyText -> strings.errorTextRequired
}

private fun submitReviewReplyErrorMessage(
    strings: ReviewStrings,
    error: SubmitReviewReplyError,
): String = when (error) {
    SubmitReviewReplyError.NotAuthenticated -> strings.errorReplyNotAuthenticated
    SubmitReviewReplyError.NotSeller -> strings.errorReplyNotSeller
    SubmitReviewReplyError.AlreadyReplied -> strings.errorReplyAlreadyReplied
    SubmitReviewReplyError.EmptyText -> strings.errorReplyTextRequired
    SubmitReviewReplyError.ReviewNotFound -> strings.errorReplyReviewNotFound
}

@Composable
private fun ReviewsEmptyState(
    title: String,
    message: String,
    modifier: Modifier = Modifier,
) {
    val dimensions = LocalDimensions.current

    Column(
        modifier = modifier.padding(dimensions.horizontalMedium),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensions.verticalXSmall),
    ) {
        Text(
            text = title,
            style = TitleMedium.copy(color = GrayText),
            textAlign = TextAlign.Center,
        )
        Text(
            text = message,
            style = BodyMedium.copy(color = GrayText),
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
@Preview
private fun ReviewsScreenPreview() {
    HvalaTheme {
        ReviewsContent(
            state = ReviewsUiState(
                sellerId = "2",
                sellerName = "Maria",
                summary = UIReviewSummary(
                    averageRating = 4.2f,
                    totalCount = 8,
                    ratingDistribution = mapOf(5 to 3, 4 to 3, 3 to 1, 2 to 0, 1 to 0),
                ),
                allReviews = listOf(
                    UIReview(
                        id = "1",
                        authorName = "Maria K.",
                        authorInitials = "MK",
                        rating = 5,
                        text = "Excellent seller! The item matched the description perfectly.",
                        postedAt = "28.05.2025",
                        postedAtIso = "2025-05-28",
                        listingTitle = "Number Nine Hoodie",
                        avatarColorArgb = 0xFFFFB74D,
                        canReply = true,
                    ),
                ),
                isLoading = false,
                canLeaveReview = true,
            ),
            onSortOrderSelected = {},
            onOnlyWithPhotosToggle = {},
            onLeaveReviewClick = {},
            onWriteReviewDismiss = {},
            onWriteReviewRatingChanged = {},
            onWriteReviewTextChanged = {},
            onWriteReviewListingSelected = {},
            onAddReviewPhotoClick = {},
            onSubmitWriteReview = {},
            onReplyClick = {},
            onReplyReviewDismiss = {},
            onReplyReviewTextChanged = {},
            onSubmitReplyReview = {},
        )
    }
}

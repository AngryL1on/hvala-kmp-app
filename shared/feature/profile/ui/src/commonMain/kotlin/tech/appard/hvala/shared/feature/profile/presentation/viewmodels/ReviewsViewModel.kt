package tech.appard.hvala.shared.feature.profile.presentation.viewmodels

import tech.appard.hvala.shared.core.mvi.MviEffect
import tech.appard.hvala.shared.core.mvi.MviIntent
import tech.appard.hvala.shared.core.mvi.MviState
import tech.appard.hvala.shared.core.mvi.MviViewModel
import tech.appard.hvala.shared.feature.auth.domain.GetCurrentProfileUseCase
import tech.appard.hvala.shared.feature.auth.domain.repository.AuthRepository
import tech.appard.hvala.shared.feature.profile.domain.GetReviewsUseCase
import tech.appard.hvala.shared.feature.profile.domain.SubmitReviewException
import tech.appard.hvala.shared.feature.profile.domain.SubmitReviewReplyException
import tech.appard.hvala.shared.feature.profile.domain.SubmitReviewReplyUseCase
import tech.appard.hvala.shared.feature.profile.domain.SubmitReviewUseCase
import tech.appard.hvala.shared.feature.profile.domain.model.SubmitReviewError
import tech.appard.hvala.shared.feature.profile.domain.model.SubmitReviewReplyError
import tech.appard.hvala.shared.feature.profile.domain.model.SubmitReviewReplyRequest
import tech.appard.hvala.shared.feature.profile.domain.model.SubmitReviewRequest
import tech.appard.hvala.shared.feature.profile.domain.repository.ReviewsRepository
import tech.appard.hvala.shared.feature.profile.domain.repository.SellerRepository
import tech.appard.hvala.shared.feature.profile.presentation.mapper.toUi
import tech.appard.hvala.shared.feature.profile.presentation.model.ReviewListingOption
import tech.appard.hvala.shared.feature.profile.presentation.model.ReviewSortOrder
import tech.appard.hvala.shared.feature.profile.presentation.model.UIReview
import tech.appard.hvala.shared.feature.profile.presentation.model.UIReviewSummary

data class WriteReviewDialogState(
    val rating: Int = 0,
    val text: String = "",
    val selectedListingId: String? = null,
    val listingOptions: List<ReviewListingOption> = emptyList(),
    val photoUris: List<String> = emptyList(),
    val isSubmitting: Boolean = false,
    val error: SubmitReviewError? = null,
)

data class ReplyReviewDialogState(
    val reviewId: String,
    val text: String = "",
    val isSubmitting: Boolean = false,
    val error: SubmitReviewReplyError? = null,
)

data class ReviewsUiState(
    val sellerId: String = "",
    val sellerName: String = "",
    val summary: UIReviewSummary? = null,
    val allReviews: List<UIReview> = emptyList(),
    val selectedSortOrder: ReviewSortOrder = ReviewSortOrder.NewestFirst,
    val onlyWithPhotos: Boolean = false,
    val isLoading: Boolean = true,
    val error: String? = null,
    val canLeaveReview: Boolean = false,
    val writeReviewDialog: WriteReviewDialogState? = null,
    val replyReviewDialog: ReplyReviewDialogState? = null,
) : MviState {
    val filteredReviews: List<UIReview>
        get() {
            val reviews = if (onlyWithPhotos) {
                allReviews.filter { it.photoUris.isNotEmpty() }
            } else {
                allReviews
            }
            return reviews.sortedWith(selectedSortOrder.comparator())
        }
}

private fun ReviewSortOrder.comparator(): Comparator<UIReview> = when (this) {
    ReviewSortOrder.NewestFirst -> compareByDescending { it.postedAtIso }
    ReviewSortOrder.OldestFirst -> compareBy { it.postedAtIso }
    ReviewSortOrder.PositiveFirst -> compareByDescending<UIReview> { it.rating }
        .thenByDescending { it.postedAtIso }
    ReviewSortOrder.NegativeFirst -> compareBy<UIReview> { it.rating }
        .thenByDescending { it.postedAtIso }
}

sealed interface ReviewsIntent : MviIntent {
    data class Load(val sellerId: String) : ReviewsIntent
    data class SortOrderSelected(val sortOrder: ReviewSortOrder) : ReviewsIntent
    data object OnlyWithPhotosToggled : ReviewsIntent
    data object OpenWriteReview : ReviewsIntent
    data object CloseWriteReview : ReviewsIntent
    data class WriteReviewRatingChanged(val rating: Int) : ReviewsIntent
    data class WriteReviewTextChanged(val text: String) : ReviewsIntent
    data class WriteReviewListingSelected(val listingId: String) : ReviewsIntent
    data class WriteReviewPhotosAdded(val uris: List<String>) : ReviewsIntent
    data object SubmitWriteReview : ReviewsIntent
    data class OpenReplyReview(val reviewId: String) : ReviewsIntent
    data object CloseReplyReview : ReviewsIntent
    data class ReplyReviewTextChanged(val text: String) : ReviewsIntent
    data object SubmitReplyReview : ReviewsIntent
}

sealed interface ReviewsEffect : MviEffect

class ReviewsViewModel(
    private val getReviewsUseCase: GetReviewsUseCase,
    private val submitReviewUseCase: SubmitReviewUseCase,
    private val submitReviewReplyUseCase: SubmitReviewReplyUseCase,
    private val authRepository: AuthRepository,
    private val getCurrentProfileUseCase: GetCurrentProfileUseCase,
    private val reviewsRepository: ReviewsRepository,
    private val sellerRepository: SellerRepository,
) : MviViewModel<ReviewsIntent, ReviewsUiState, ReviewsEffect>(ReviewsUiState()) {

    override suspend fun handleIntent(intent: ReviewsIntent) {
        when (intent) {
            is ReviewsIntent.Load -> loadReviews(intent.sellerId)
            is ReviewsIntent.SortOrderSelected -> {
                updateState { it.copy(selectedSortOrder = intent.sortOrder) }
            }
            ReviewsIntent.OnlyWithPhotosToggled -> {
                updateState { it.copy(onlyWithPhotos = !it.onlyWithPhotos) }
            }
            ReviewsIntent.OpenWriteReview -> openWriteReview()
            ReviewsIntent.CloseWriteReview -> closeWriteReview()
            is ReviewsIntent.WriteReviewRatingChanged -> updateWriteReviewDialog {
                it.copy(rating = intent.rating, error = null)
            }
            is ReviewsIntent.WriteReviewTextChanged -> updateWriteReviewDialog {
                it.copy(text = intent.text, error = null)
            }
            is ReviewsIntent.WriteReviewListingSelected -> updateWriteReviewDialog {
                it.copy(selectedListingId = intent.listingId.takeIf { id -> id.isNotBlank() })
            }
            is ReviewsIntent.WriteReviewPhotosAdded -> updateWriteReviewDialog {
                val merged = (it.photoUris + intent.uris).distinct().take(MAX_REVIEW_PHOTOS)
                it.copy(photoUris = merged, error = null)
            }
            ReviewsIntent.SubmitWriteReview -> submitWriteReview()
            is ReviewsIntent.OpenReplyReview -> openReplyReview(intent.reviewId)
            ReviewsIntent.CloseReplyReview -> closeReplyReview()
            is ReviewsIntent.ReplyReviewTextChanged -> updateReplyReviewDialog {
                it.copy(text = intent.text, error = null)
            }
            ReviewsIntent.SubmitReplyReview -> submitReplyReview()
        }
    }

    fun load(sellerId: String) = onIntent(ReviewsIntent.Load(sellerId))
    fun onSortOrderSelected(sortOrder: ReviewSortOrder) = onIntent(ReviewsIntent.SortOrderSelected(sortOrder))
    fun onOnlyWithPhotosToggle() = onIntent(ReviewsIntent.OnlyWithPhotosToggled)
    fun onLeaveReviewClick() = onIntent(ReviewsIntent.OpenWriteReview)
    fun onWriteReviewDismiss() = onIntent(ReviewsIntent.CloseWriteReview)
    fun onWriteReviewRatingChanged(rating: Int) = onIntent(ReviewsIntent.WriteReviewRatingChanged(rating))
    fun onWriteReviewTextChanged(text: String) = onIntent(ReviewsIntent.WriteReviewTextChanged(text))
    fun onWriteReviewListingSelected(listingId: String) = onIntent(ReviewsIntent.WriteReviewListingSelected(listingId))
    fun onWriteReviewPhotosAdded(uris: List<String>) = onIntent(ReviewsIntent.WriteReviewPhotosAdded(uris))
    fun onSubmitWriteReview() = onIntent(ReviewsIntent.SubmitWriteReview)
    fun onReplyClick(reviewId: String) = onIntent(ReviewsIntent.OpenReplyReview(reviewId))
    fun onReplyReviewDismiss() = onIntent(ReviewsIntent.CloseReplyReview)
    fun onReplyReviewTextChanged(text: String) = onIntent(ReviewsIntent.ReplyReviewTextChanged(text))
    fun onSubmitReplyReview() = onIntent(ReviewsIntent.SubmitReplyReview)

    private suspend fun loadReviews(sellerId: String, force: Boolean = false) {
        if (!force && currentState().sellerId == sellerId && !currentState().isLoading && currentState().summary != null) {
            return
        }
        val previousWriteDialog = currentState().writeReviewDialog
        val previousReplyDialog = currentState().replyReviewDialog
        val previousSortOrder = currentState().selectedSortOrder
        val previousOnlyWithPhotos = currentState().onlyWithPhotos
        updateState {
            ReviewsUiState(
                sellerId = sellerId,
                isLoading = true,
                selectedSortOrder = previousSortOrder,
                onlyWithPhotos = previousOnlyWithPhotos,
                writeReviewDialog = previousWriteDialog,
                replyReviewDialog = previousReplyDialog,
            )
        }
        val canReplyToReviews = computeCanReplyToReviews(sellerId)
        val bundle = getReviewsUseCase(sellerId).toUi(canReplyToReviews = canReplyToReviews)
        val canLeaveReview = computeCanLeaveReview(sellerId)
        updateState {
            ReviewsUiState(
                sellerId = bundle.sellerId,
                sellerName = bundle.sellerName,
                summary = bundle.summary,
                allReviews = bundle.reviews,
                isLoading = false,
                canLeaveReview = canLeaveReview,
                selectedSortOrder = previousSortOrder,
                onlyWithPhotos = previousOnlyWithPhotos,
                writeReviewDialog = previousWriteDialog,
                replyReviewDialog = previousReplyDialog,
            )
        }
    }

    private suspend fun computeCanLeaveReview(sellerId: String): Boolean {
        if (!authRepository.isAuthenticated()) return false
        val profile = getCurrentProfileUseCase()
        if (profile.id == sellerId) return false
        reviewsRepository.ensureLoaded()
        return !reviewsRepository.hasReviewFromAuthor(sellerId, profile.id)
    }

    private suspend fun computeCanReplyToReviews(sellerId: String): Boolean {
        if (!authRepository.isAuthenticated()) return false
        val profile = getCurrentProfileUseCase()
        return profile.id == sellerId
    }

    private suspend fun openWriteReview() {
        if (!currentState().canLeaveReview) return
        sellerRepository.ensureLoaded()
        val listingOptions = sellerRepository.getListingsForSeller(currentState().sellerId)
            .map { listing -> ReviewListingOption(id = listing.title, label = listing.title) }
        updateState {
            it.copy(
                writeReviewDialog = WriteReviewDialogState(listingOptions = listingOptions),
            )
        }
    }

    private fun closeWriteReview() {
        updateState { it.copy(writeReviewDialog = null) }
    }

    private fun openReplyReview(reviewId: String) {
        val review = currentState().allReviews.find { it.id == reviewId } ?: return
        if (!review.canReply) return
        updateState {
            it.copy(replyReviewDialog = ReplyReviewDialogState(reviewId = reviewId))
        }
    }

    private fun closeReplyReview() {
        updateState { it.copy(replyReviewDialog = null) }
    }

    private suspend fun submitWriteReview() {
        val dialog = currentState().writeReviewDialog ?: return
        val sellerId = currentState().sellerId
        val selectedListingTitle = dialog.listingOptions
            .firstOrNull { it.id == dialog.selectedListingId }
            ?.label

        updateWriteReviewDialog { it.copy(isSubmitting = true, error = null) }

        val result = submitReviewUseCase(
            SubmitReviewRequest(
                sellerId = sellerId,
                rating = dialog.rating,
                text = dialog.text,
                listingTitle = selectedListingTitle,
                photoUris = dialog.photoUris,
            ),
        )

        result.fold(
            onSuccess = {
                closeWriteReview()
                loadReviews(sellerId, force = true)
            },
            onFailure = { error ->
                val submitError = (error as? SubmitReviewException)?.error
                    ?: SubmitReviewError.InvalidRating
                updateWriteReviewDialog {
                    it.copy(isSubmitting = false, error = submitError)
                }
            },
        )
    }

    private suspend fun submitReplyReview() {
        val dialog = currentState().replyReviewDialog ?: return
        val sellerId = currentState().sellerId

        updateReplyReviewDialog { it.copy(isSubmitting = true, error = null) }

        val result = submitReviewReplyUseCase(
            SubmitReviewReplyRequest(
                sellerId = sellerId,
                reviewId = dialog.reviewId,
                text = dialog.text,
            ),
        )

        result.fold(
            onSuccess = {
                closeReplyReview()
                loadReviews(sellerId, force = true)
            },
            onFailure = { error ->
                val replyError = (error as? SubmitReviewReplyException)?.error
                    ?: SubmitReviewReplyError.EmptyText
                updateReplyReviewDialog {
                    it.copy(isSubmitting = false, error = replyError)
                }
            },
        )
    }

    private inline fun updateWriteReviewDialog(
        crossinline transform: (WriteReviewDialogState) -> WriteReviewDialogState,
    ) {
        updateState { state ->
            val dialog = state.writeReviewDialog ?: return@updateState state
            state.copy(writeReviewDialog = transform(dialog))
        }
    }

    private inline fun updateReplyReviewDialog(
        crossinline transform: (ReplyReviewDialogState) -> ReplyReviewDialogState,
    ) {
        updateState { state ->
            val dialog = state.replyReviewDialog ?: return@updateState state
            state.copy(replyReviewDialog = transform(dialog))
        }
    }

    companion object {
        const val MAX_REVIEW_PHOTOS = 3
    }
}

typealias ReviewsStateHolder = ReviewsViewModel

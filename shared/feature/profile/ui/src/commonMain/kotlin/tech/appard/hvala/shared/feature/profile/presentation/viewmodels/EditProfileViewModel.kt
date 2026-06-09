package tech.appard.hvala.shared.feature.profile.presentation.viewmodels

import tech.appard.hvala.shared.core.i18n.AuthStrings
import tech.appard.hvala.shared.core.i18n.strings
import tech.appard.hvala.shared.core.mvi.MviEffect
import tech.appard.hvala.shared.core.mvi.MviIntent
import tech.appard.hvala.shared.core.mvi.MviState
import tech.appard.hvala.shared.core.mvi.MviViewModel
import tech.appard.hvala.shared.feature.auth.domain.GetCurrentProfileUseCase
import tech.appard.hvala.shared.feature.auth.domain.UpdateProfileUseCase
import tech.appard.hvala.shared.feature.auth.domain.model.UserProfile
import tech.appard.hvala.shared.feature.settings.domain.repository.LocaleRepository

data class EditProfileUiState(
    val profileId: String = "",
    val fullName: String = "",
    val email: String = "",
    val phone: String = "",
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val error: String? = null,
) : MviState

sealed interface EditProfileIntent : MviIntent {
    data object Load : EditProfileIntent
    data class FullNameChanged(val value: String) : EditProfileIntent
    data class EmailChanged(val value: String) : EditProfileIntent
    data class PhoneChanged(val value: String) : EditProfileIntent
    data object Save : EditProfileIntent
}

sealed interface EditProfileEffect : MviEffect {
    data object Saved : EditProfileEffect
}

class EditProfileViewModel(
    private val localeRepository: LocaleRepository,
    private val getCurrentProfileUseCase: GetCurrentProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
) : MviViewModel<EditProfileIntent, EditProfileUiState, EditProfileEffect>(EditProfileUiState()) {

    private val authStrings: AuthStrings
        get() = localeRepository.getLanguage().strings().auth

    override suspend fun handleIntent(intent: EditProfileIntent) {
        when (intent) {
            EditProfileIntent.Load -> loadProfile()
            is EditProfileIntent.FullNameChanged -> {
                updateState { it.copy(fullName = intent.value, error = null) }
            }
            is EditProfileIntent.EmailChanged -> {
                updateState { it.copy(email = intent.value, error = null) }
            }
            is EditProfileIntent.PhoneChanged -> {
                updateState { it.copy(phone = intent.value, error = null) }
            }
            EditProfileIntent.Save -> saveProfile()
        }
    }

    fun load() = onIntent(EditProfileIntent.Load)
    fun onFullNameChange(value: String) = onIntent(EditProfileIntent.FullNameChanged(value))
    fun onEmailChange(value: String) = onIntent(EditProfileIntent.EmailChanged(value))
    fun onPhoneChange(value: String) = onIntent(EditProfileIntent.PhoneChanged(value))
    fun save() = onIntent(EditProfileIntent.Save)

    private suspend fun loadProfile() {
        if (!currentState().isLoading && currentState().profileId.isNotBlank()) return
        updateState { it.copy(isLoading = true, error = null) }
        val profile = getCurrentProfileUseCase()
        updateState {
            EditProfileUiState(
                profileId = profile.id,
                fullName = profile.fullName,
                email = profile.email,
                phone = profile.phone,
                isLoading = false,
            )
        }
    }

    private suspend fun saveProfile() {
        val snapshot = currentState()
        if (snapshot.isSaving) return

        val validationError = validate(snapshot)
        if (validationError != null) {
            updateState { it.copy(error = validationError) }
            return
        }

        updateState { it.copy(isSaving = true, error = null) }
        val currentProfile = getCurrentProfileUseCase()
        updateProfileUseCase(
            UserProfile(
                id = snapshot.profileId,
                fullName = snapshot.fullName.trim(),
                email = snapshot.email.trim(),
                phone = snapshot.phone.trim(),
                avatarUrl = currentProfile.avatarUrl,
            ),
        )
        updateState { it.copy(isSaving = false) }
        sendEffect(EditProfileEffect.Saved)
    }

    private fun validate(state: EditProfileUiState): String? = when {
        state.fullName.isBlank() -> authStrings.errorNameRequired
        !state.email.contains("@") -> authStrings.errorEmailInvalid
        state.phone.filter(Char::isDigit).length < 10 -> authStrings.errorPhoneInvalid
        else -> null
    }
}

typealias EditProfileStateHolder = EditProfileViewModel

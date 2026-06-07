package tech.appard.hvala.shared.feature.profile

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tech.appard.hvala.shared.feature.auth.api.repository.AuthRepository
import tech.appard.hvala.shared.feature.profile.api.model.UserProfile
import tech.appard.hvala.shared.feature.profile.api.repository.ProfileRepository

data class ProfileUiState(
    val profile: UserProfile? = null,
    val isLoading: Boolean = false,
)

class ProfileStateHolder(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository,
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val _state = MutableStateFlow(ProfileUiState())
    val state: StateFlow<ProfileUiState> = _state.asStateFlow()

    private val _events = MutableSharedFlow<ProfileUiEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<ProfileUiEvent> = _events.asSharedFlow()

    fun reset() {
        _state.value = ProfileUiState()
    }

    fun load() {
        if (_state.value.profile != null || _state.value.isLoading) return
        scope.launch {
            _state.update { it.copy(isLoading = true) }
            val profile = profileRepository.getCurrentProfile()
            _state.update { it.copy(profile = profile, isLoading = false) }
        }
    }

    fun logout() {
        scope.launch {
            authRepository.signOut()
            reset()
            _events.emit(ProfileUiEvent.LoggedOut)
        }
    }
}

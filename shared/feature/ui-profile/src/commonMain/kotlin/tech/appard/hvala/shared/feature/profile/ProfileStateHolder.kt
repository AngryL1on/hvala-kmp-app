package tech.appard.hvala.shared.feature.profile

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tech.appard.hvala.shared.core.contracts.model.UserProfile
import tech.appard.hvala.shared.core.contracts.repository.ProfileRepository

data class ProfileUiState(
    val profile: UserProfile? = null,
    val isLoading: Boolean = false,
)

class ProfileStateHolder(
    private val profileRepository: ProfileRepository,
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val _state = MutableStateFlow(ProfileUiState())
    val state: StateFlow<ProfileUiState> = _state.asStateFlow()

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
}

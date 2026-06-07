package tech.appard.hvala.shared.feature.auth

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
import tech.appard.hvala.shared.feature.auth.api.model.AuthCredentials
import tech.appard.hvala.shared.feature.auth.api.repository.AuthRepository

data class AuthUiState(
    val login: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
)

class AuthStateHolder(
    private val authRepository: AuthRepository,
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val _state = MutableStateFlow(AuthUiState())
    val state: StateFlow<AuthUiState> = _state.asStateFlow()

    private val _events = MutableSharedFlow<AuthUiEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<AuthUiEvent> = _events.asSharedFlow()

    fun onLoginChange(value: String) {
        _state.update { it.copy(login = value) }
    }

    fun onPasswordChange(value: String) {
        _state.update { it.copy(password = value) }
    }

    fun reset() {
        _state.value = AuthUiState()
    }

    fun signIn() {
        val snapshot = _state.value
        if (snapshot.isLoading) return

        scope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            val success = authRepository.signIn(
                AuthCredentials(
                    login = snapshot.login.trim(),
                    password = snapshot.password,
                ),
            )
            if (success) {
                _state.update { it.copy(isLoading = false, error = null) }
                _events.emit(AuthUiEvent.Authenticated)
            } else {
                _state.update { it.copy(isLoading = false, error = "Invalid credentials") }
            }
        }
    }
}

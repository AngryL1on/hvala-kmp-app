package tech.appard.hvala.shared.feature.auth

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tech.appard.hvala.shared.core.contracts.model.AuthCredentials
import tech.appard.hvala.shared.core.contracts.repository.AuthRepository

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

    private val _isAuthenticated = MutableStateFlow(authRepository.isAuthenticated())
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated.asStateFlow()

    fun onLoginChange(value: String) {
        _state.update { it.copy(login = value, error = null) }
    }

    fun onPasswordChange(value: String) {
        _state.update { it.copy(password = value, error = null) }
    }

    fun reset() {
        _state.value = AuthUiState()
    }

    fun signOut() {
        scope.launch {
            authRepository.signOut()
            _isAuthenticated.value = false
            reset()
        }
    }

    fun signIn(onSuccess: () -> Unit) {
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
                _isAuthenticated.value = true
                _state.update { it.copy(isLoading = false, error = null) }
                onSuccess()
            } else {
                _state.update { it.copy(isLoading = false, error = "Invalid credentials") }
            }
        }
    }
}

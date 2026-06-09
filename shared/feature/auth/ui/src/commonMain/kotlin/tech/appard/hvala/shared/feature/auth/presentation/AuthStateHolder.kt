package tech.appard.hvala.shared.feature.auth.presentation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tech.appard.hvala.shared.core.i18n.strings
import tech.appard.hvala.shared.feature.auth.domain.model.AuthCredentials
import tech.appard.hvala.shared.feature.auth.domain.model.RegistrationData
import tech.appard.hvala.shared.feature.auth.domain.repository.AuthRepository
import tech.appard.hvala.shared.feature.settings.domain.repository.LocaleRepository

data class AuthUiState(
    val login: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
)

data class RegistrationUiState(
    val fullName: String = "",
    val email: String = "",
    val phone: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isTermsAccepted: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
)

class AuthStateHolder(
    private val authRepository: AuthRepository,
    private val localeRepository: LocaleRepository,
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val _state = MutableStateFlow(AuthUiState())
    val state: StateFlow<AuthUiState> = _state.asStateFlow()

    private val _registrationState = MutableStateFlow(RegistrationUiState())
    val registrationState: StateFlow<RegistrationUiState> = _registrationState.asStateFlow()

    private val _isAuthenticated = MutableStateFlow(authRepository.isAuthenticated())
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated.asStateFlow()

    fun onLoginChange(value: String) {
        _state.update { it.copy(login = value, error = null) }
    }

    fun onPasswordChange(value: String) {
        _state.update { it.copy(password = value, error = null) }
    }

    fun onRegistrationFullNameChange(value: String) {
        _registrationState.update { it.copy(fullName = value, error = null) }
    }

    fun onRegistrationEmailChange(value: String) {
        _registrationState.update { it.copy(email = value, error = null) }
    }

    fun onRegistrationPhoneChange(value: String) {
        _registrationState.update { it.copy(phone = value, error = null) }
    }

    fun onRegistrationPasswordChange(value: String) {
        _registrationState.update { it.copy(password = value, error = null) }
    }

    fun onRegistrationConfirmPasswordChange(value: String) {
        _registrationState.update { it.copy(confirmPassword = value, error = null) }
    }

    fun onRegistrationTermsAcceptedChange(value: Boolean) {
        _registrationState.update { it.copy(isTermsAccepted = value, error = null) }
    }

    fun reset() {
        _state.value = AuthUiState()
    }

    fun resetRegistration() {
        _registrationState.value = RegistrationUiState()
    }

    fun signOut() {
        scope.launch {
            authRepository.signOut()
            _isAuthenticated.value = false
            reset()
            resetRegistration()
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
                val authStrings = localeRepository.getLanguage().strings().auth
                _state.update { it.copy(isLoading = false, error = authStrings.invalidCredentials) }
            }
        }
    }

    fun signUp(onSuccess: () -> Unit) {
        val snapshot = _registrationState.value
        if (snapshot.isLoading) return

        val validationError = validateRegistration(snapshot)
        if (validationError != null) {
            _registrationState.update { it.copy(error = validationError) }
            return
        }

        scope.launch {
            _registrationState.update { it.copy(isLoading = true, error = null) }
            val success = authRepository.signUp(
                RegistrationData(
                    fullName = snapshot.fullName.trim(),
                    email = snapshot.email.trim(),
                    phone = snapshot.phone,
                    password = snapshot.password,
                ),
            )
            if (success) {
                _isAuthenticated.value = true
                _registrationState.update { it.copy(isLoading = false, error = null) }
                onSuccess()
            } else {
                val authStrings = localeRepository.getLanguage().strings().auth
                _registrationState.update {
                    it.copy(isLoading = false, error = authStrings.registrationFailed)
                }
            }
        }
    }

    private fun validateRegistration(state: RegistrationUiState): String? {
        val authStrings = localeRepository.getLanguage().strings().auth
        return when {
            state.fullName.isBlank() -> authStrings.errorNameRequired
            !state.email.contains("@") -> authStrings.errorEmailInvalid
            state.phone.filter(Char::isDigit).length < 10 -> authStrings.errorPhoneInvalid
            state.password.length < 4 -> authStrings.errorPasswordTooShort
            state.password != state.confirmPassword -> authStrings.errorPasswordMismatch
            !state.isTermsAccepted -> authStrings.errorTermsRequired
            else -> null
        }
    }
}

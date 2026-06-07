package tech.appard.hvala.shared.feature.auth

sealed interface AuthUiEvent {
    data object Authenticated : AuthUiEvent
}

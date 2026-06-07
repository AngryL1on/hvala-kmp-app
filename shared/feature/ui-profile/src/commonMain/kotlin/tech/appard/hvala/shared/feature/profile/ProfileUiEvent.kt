package tech.appard.hvala.shared.feature.profile

sealed interface ProfileUiEvent {
    data object LoggedOut : ProfileUiEvent
}

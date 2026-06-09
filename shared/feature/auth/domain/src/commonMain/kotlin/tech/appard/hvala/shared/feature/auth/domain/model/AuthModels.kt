package tech.appard.hvala.shared.feature.auth.domain.model

data class UserProfile(
    val id: String,
    val fullName: String,
    val email: String,
    val phone: String = "",
    val avatarUrl: String = "",
)

data class AuthCredentials(
    val login: String,
    val password: String,
)

data class RegistrationData(
    val fullName: String,
    val email: String,
    val phone: String,
    val password: String,
)

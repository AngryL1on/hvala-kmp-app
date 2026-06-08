package tech.appard.hvala.shared.feature.auth.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UserProfileDto(
    val id: String,
    val fullName: String,
    val email: String,
)

@Serializable
data class AuthCredentialsDto(
    val login: String,
    val password: String,
)

@Serializable
data class RegistrationDataDto(
    val fullName: String,
    val email: String,
    val phone: String,
    val password: String,
)

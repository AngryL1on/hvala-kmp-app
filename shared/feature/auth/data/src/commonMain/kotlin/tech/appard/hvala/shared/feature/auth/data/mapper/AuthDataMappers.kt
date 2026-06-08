package tech.appard.hvala.shared.feature.auth.data.mapper

import tech.appard.hvala.shared.feature.auth.data.model.AuthCredentialsDto
import tech.appard.hvala.shared.feature.auth.data.model.RegistrationDataDto
import tech.appard.hvala.shared.feature.auth.data.model.UserProfileDto
import tech.appard.hvala.shared.feature.auth.domain.model.AuthCredentials
import tech.appard.hvala.shared.feature.auth.domain.model.RegistrationData
import tech.appard.hvala.shared.feature.auth.domain.model.UserProfile

fun UserProfileDto.toDomain(): UserProfile = UserProfile(
    id = id,
    fullName = fullName,
    email = email,
)

fun UserProfile.toDto(): UserProfileDto = UserProfileDto(
    id = id,
    fullName = fullName,
    email = email,
)

fun AuthCredentialsDto.toDomain(): AuthCredentials = AuthCredentials(
    login = login,
    password = password,
)

fun AuthCredentials.toDto(): AuthCredentialsDto = AuthCredentialsDto(
    login = login,
    password = password,
)

fun RegistrationDataDto.toDomain(): RegistrationData = RegistrationData(
    fullName = fullName,
    email = email,
    phone = phone,
    password = password,
)

fun RegistrationData.toDto(): RegistrationDataDto = RegistrationDataDto(
    fullName = fullName,
    email = email,
    phone = phone,
    password = password,
)

package tech.appard.hvala.shared.core.datastore.session

import kotlinx.serialization.Serializable
import tech.appard.hvala.shared.feature.auth.domain.model.UserProfile

@Serializable
internal data class StoredUserProfile(
    val id: String,
    val fullName: String,
    val email: String,
    val phone: String = "",
    val avatarUrl: String = "",
)

internal fun UserProfile.toStored(): StoredUserProfile = StoredUserProfile(
    id = id,
    fullName = fullName,
    email = email,
    phone = phone,
    avatarUrl = avatarUrl,
)

internal fun StoredUserProfile.toDomain(): UserProfile = UserProfile(
    id = id,
    fullName = fullName,
    email = email,
    phone = phone,
    avatarUrl = avatarUrl,
)

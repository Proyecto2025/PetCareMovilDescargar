package net.iessochoa.vanesa.petcare.data.model.dto.user

import kotlinx.serialization.Serializable

@Serializable
data class LoginUserDto(
    val id: Long,
    val fullName: String,
    val userName: String,
    val email: String
)
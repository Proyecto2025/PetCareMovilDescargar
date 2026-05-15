package net.iessochoa.vanesa.petcare.data.model.dto.request.user;

import kotlinx.serialization.Serializable

@Serializable
data class CreateUserDtoReq(
    val fullName: String,
    val userName: String,
    val phoneNumber: String,
    val email: String,
    val password: String,
    val confirmPassword: String
)

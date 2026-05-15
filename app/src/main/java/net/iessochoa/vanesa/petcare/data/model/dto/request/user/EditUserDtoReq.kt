package net.iessochoa.vanesa.petcare.data.model.dto.request.user;

import kotlinx.serialization.Serializable

@Serializable
data class EditUserDtoReq(
    val fullName: String,
    val userName: String,
    val phoneNumber: String,
    val email: String
)
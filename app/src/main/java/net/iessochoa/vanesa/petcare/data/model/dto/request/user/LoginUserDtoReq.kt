package net.iessochoa.vanesa.petcare.data.model.dto.request.user;

import kotlinx.serialization.Serializable

@Serializable
data class LoginUserDtoReq(
    val userName: String,
    val password: String
)

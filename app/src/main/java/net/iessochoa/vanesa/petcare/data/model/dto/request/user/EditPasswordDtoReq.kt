package net.iessochoa.vanesa.petcare.data.model.dto.request.user;


import kotlinx.serialization.Serializable

@Serializable
data class EditPasswordDtoReq(
    val password: String,
    val confirmPassword: String
)

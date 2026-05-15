package net.iessochoa.vanesa.petcare.data.model.dto.user

import kotlinx.serialization.Serializable

//RECIBE DATOS = DTO (EL REQ ENVIA DATOS AL BACKEND)
@Serializable
data class CreateUserDto(
    val id: Long,
    val fullName: String,
    val userName: String,
    val phoneNumber: String,
    val email: String
)
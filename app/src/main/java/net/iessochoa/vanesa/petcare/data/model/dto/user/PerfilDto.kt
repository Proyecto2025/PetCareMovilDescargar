package net.iessochoa.vanesa.petcare.data.model.dto.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PerfilDto(
    @SerialName("id") val id: Long,
    @SerialName("fullName") val fullName: String,
    @SerialName("phoneNumber") val phoneNumber: String,
    @SerialName("email") val email: String,
    @SerialName("userName") val userName: String,
    @SerialName("numberOfPosts") val numberOfPosts: Int,
    @SerialName("numberOfAdvices") val numberOfAdvices: Int
)

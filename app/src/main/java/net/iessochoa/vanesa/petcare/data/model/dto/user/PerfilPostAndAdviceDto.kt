package net.iessochoa.vanesa.petcare.data.model.dto.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PerfilPostAndAdviceDto(
    val id: Long,
    @SerialName("title") val title: String,
    @SerialName("shortDescription") val shortDescription: String,
    @SerialName("image") val image: String
)

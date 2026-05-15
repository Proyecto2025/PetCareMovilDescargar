package net.iessochoa.vanesa.petcare.data.model.dto.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PerfilFiltradoDto(
    @SerialName("userName") val userName: String,
    @SerialName("count") val count: Int?,
    @SerialName("posts") val posts: List<PerfilPostAndAdviceDto>?,
    @SerialName("advices") val advices: List<PerfilPostAndAdviceDto>?
)

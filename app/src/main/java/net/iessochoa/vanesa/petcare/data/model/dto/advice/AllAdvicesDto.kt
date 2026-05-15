package net.iessochoa.vanesa.petcare.data.model.dto.advice

import kotlinx.serialization.Serializable
import net.iessochoa.vanesa.petcare.data.model.enums.AdviceCategory

@Serializable
data class AllAdvicesDto(
    val id: Long,
    val userName: String,
    val category: AdviceCategory,
    val title: String,
    val subtitle: String,
    val image: String,
    val shortDescription: String
)

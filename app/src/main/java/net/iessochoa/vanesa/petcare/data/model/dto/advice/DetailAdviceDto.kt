package net.iessochoa.vanesa.petcare.data.model.dto.advice

import kotlinx.serialization.Serializable
import net.iessochoa.vanesa.petcare.data.model.enums.AdviceCategory

@Serializable
data class DetailAdviceDto(
    val id: Long,
    val title: String,
    val subtitle: String,
    val adviceCategory: AdviceCategory,
    val image: String,
    val longDescription: String,
    val extraDetail: String? = null
)

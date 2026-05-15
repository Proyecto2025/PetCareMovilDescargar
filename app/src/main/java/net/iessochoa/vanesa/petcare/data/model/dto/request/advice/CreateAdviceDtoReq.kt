package net.iessochoa.vanesa.petcare.data.model.dto.request.advice

import kotlinx.serialization.Serializable
import net.iessochoa.vanesa.petcare.data.model.enums.AdviceCategory

@Serializable
data class CreateAdviceDtoReq(
    val category: AdviceCategory,
    val title: String,
    val subtitle: String,
    val shortDescription: String,
    val longDescription: String,
    val extraDetail: String? = null,
    val userId: Long
)

package net.iessochoa.vanesa.petcare.data.model.dto.post

import kotlinx.serialization.Serializable

@Serializable
data class DetailPostDto(
    val id: Long,
    val userId: Long,
    val title: String,
    val subtitle: String,
    val location: String,
    val municipality: String,
    val image: String,
    val longDescription: String,
    val extraDetails: String? = null,
    val belongings: List<String>? = null
)

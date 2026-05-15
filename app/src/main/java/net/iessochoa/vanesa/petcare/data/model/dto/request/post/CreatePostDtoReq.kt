package net.iessochoa.vanesa.petcare.data.model.dto.request.post

import kotlinx.serialization.Serializable
import net.iessochoa.vanesa.petcare.data.model.enums.PostCategory
import net.iessochoa.vanesa.petcare.data.model.enums.TypeAnimal

@Serializable
data class CreatePostDtoReq(
    val postCategory: PostCategory,
    val typeAnimal: TypeAnimal,
    val title: String,
    val subtitle: String,
    val shortDescription: String,
    val longDescription: String,
    val extraDetails: String? = null,
    val location: String,
    val municipality: String,
    val userId: Long
)

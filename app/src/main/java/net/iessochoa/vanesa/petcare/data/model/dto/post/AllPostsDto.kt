package net.iessochoa.vanesa.petcare.data.model.dto.post

import kotlinx.serialization.Serializable
import net.iessochoa.vanesa.petcare.data.model.enums.PostCategory
import net.iessochoa.vanesa.petcare.data.model.enums.TypeAnimal

@Serializable
data class AllPostsDto(
    val id: Long,
    val typeAnimal: TypeAnimal,
    val categoryPost: PostCategory,
    val title: String,
    val subtitle: String,
    val userName: String,
    val location: String,
    val municipality: String,
    val shortDescription: String,
    val image: String
)

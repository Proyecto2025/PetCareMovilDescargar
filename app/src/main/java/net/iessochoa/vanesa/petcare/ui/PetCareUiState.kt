package net.iessochoa.vanesa.petcare.ui

import net.iessochoa.vanesa.petcare.data.model.dto.advice.AllAdvicesDto
import net.iessochoa.vanesa.petcare.data.model.dto.post.AllPostsDto

data class PetCareUiState(
    val advices: List<AllAdvicesDto> = emptyList(),
    val posts: List<AllPostsDto> = emptyList(),
    val selectedAdvice: AllAdvicesDto? = null,
    val selectedPost: AllPostsDto? = null
)

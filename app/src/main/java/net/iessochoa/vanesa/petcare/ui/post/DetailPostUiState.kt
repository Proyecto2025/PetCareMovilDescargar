package net.iessochoa.vanesa.petcare.ui.post

import net.iessochoa.vanesa.petcare.model.Post

//Estado para la pantalla de detalle de Post
data class DetailPostUiState(
    val isLoading: Boolean = true,
    val post: Post? = null
)
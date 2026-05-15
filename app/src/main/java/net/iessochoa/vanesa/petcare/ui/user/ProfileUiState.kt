package net.iessochoa.vanesa.petcare.ui.user

import net.iessochoa.vanesa.petcare.data.model.dto.user.PerfilDto
import net.iessochoa.vanesa.petcare.data.model.dto.user.PerfilPostAndAdviceDto
import net.iessochoa.vanesa.petcare.model.CategoriaPost

data class ProfileUiState(
    val user: PerfilDto? = null,                       // Datos del usuario
    val filtro: CategoriaPost? = null,                 // Filtro actual

    val posts: List<PerfilPostAndAdviceDto> = emptyList(),   // Posts filtrados
    val advices: List<PerfilPostAndAdviceDto> = emptyList(), // Consejos filtrados

    val isLoadingPerfil: Boolean = false,              // Loading SOLO para contadores
    val isLoadingLista: Boolean = false,               // Loading SOLO para la lista
    val isUpdating: Boolean = false,                   // Loading al editar perfil

    val mensajeExito: String? = null                   // Mensaje opcional
)

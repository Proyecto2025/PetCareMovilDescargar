package net.iessochoa.vanesa.petcare.ui.advice

import net.iessochoa.vanesa.petcare.data.model.dto.advice.AllAdvicesDto

data class AdviceUiState(
    val advices: List<AllAdvicesDto> = emptyList(),
    val listaFiltrada: List<AllAdvicesDto> = emptyList(),
    val filtro: String = "TODO",
    val isLoading: Boolean = true
)
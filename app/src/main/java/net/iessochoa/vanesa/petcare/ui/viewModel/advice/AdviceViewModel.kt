package net.iessochoa.vanesa.petcare.ui.viewModel.advice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import net.iessochoa.vanesa.petcare.data.api.repository.PetCareRepository
import net.iessochoa.vanesa.petcare.data.model.enums.AdviceCategory
import net.iessochoa.vanesa.petcare.ui.advice.AdviceUiState

class AdviceViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AdviceUiState())
    val uiState: StateFlow<AdviceUiState> = _uiState

    init {
        //Cargar advices desde la API real al iniciar
        cargarAdvicesDesdeApi()
    }


    //Recargar Advices (se usa al volver desde crear un advice)

    fun getAllAdvices() {
        cargarAdvicesDesdeApi()
    }


    //Cargar TODOS los advices desde la API y mantiene los filtros
    private fun cargarAdvicesDesdeApi() {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                val response = PetCareRepository.getAllAdvices()

                val filtroActual = _uiState.value.filtro.uppercase()

                val listaFiltrada =
                    if (filtroActual.isEmpty() || filtroActual == "TODO") {
                        response.content
                    } else {
                        response.content.filter { it.category.name == filtroActual }
                    }


                _uiState.update {
                    it.copy(
                        advices = response.content,
                        listaFiltrada = listaFiltrada,
                        isLoading = false
                    )
                }

            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }


    //Cambiar filtro de categoría
    fun cambiarFiltro(nuevo: String) {

        val normalized = nuevo.uppercase()

        val filtrada = when (normalized) {
            "COMIDA" -> _uiState.value.advices.filter { it.category == AdviceCategory.COMIDA }
            "HIGIENE" -> _uiState.value.advices.filter { it.category == AdviceCategory.HIGIENE }
            "ACCESORIOS" -> _uiState.value.advices.filter { it.category == AdviceCategory.ACCESORIOS }
            else -> _uiState.value.advices
        }

        _uiState.update {
            it.copy(
                filtro = normalized,
                listaFiltrada = filtrada
            )
        }
    }
}

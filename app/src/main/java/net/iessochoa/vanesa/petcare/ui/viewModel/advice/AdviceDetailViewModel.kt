package net.iessochoa.vanesa.petcare.ui.viewModel.advice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import net.iessochoa.vanesa.petcare.data.api.repository.PetCareRepository
import net.iessochoa.vanesa.petcare.data.model.dto.advice.DetailAdviceDto

/**
 * Cargar UN advice por ID desde la API
 */
class AdviceDetailViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<DetailAdviceDto?>(null)
    val uiState: StateFlow<DetailAdviceDto?> = _uiState

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun loadAdvice(id: Long) {
        viewModelScope.launch {

            _isLoading.value = true   //EMPIEZA CARGA
            try {
                val advice = PetCareRepository.getAdviceDetail(id)
                _uiState.value = advice
            } catch (e: Exception) {
                _uiState.value = null
            }finally {
                _isLoading.value = false   //TERMINA CARGA
            }
        }
    }


}
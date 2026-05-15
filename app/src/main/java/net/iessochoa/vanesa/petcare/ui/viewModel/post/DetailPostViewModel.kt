package net.iessochoa.vanesa.petcare.ui.viewModel.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import net.iessochoa.vanesa.petcare.data.api.repository.PetCareRepository
import net.iessochoa.vanesa.petcare.data.api.repository.PostRepository
import net.iessochoa.vanesa.petcare.ui.post.DetailPostUiState

/**
 * Cargar UN post por ID desde la API
 */
class DetailPostViewModel(
    private val postId: Long
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailPostUiState())
    val uiState: StateFlow<DetailPostUiState> = _uiState

    private val _userEmail = MutableStateFlow<String?>(null)
    val userEmail: StateFlow<String?> = _userEmail

    init {
        viewModelScope.launch {

            _uiState.update { it.copy(isLoading = true) }

            try {
                val post = PostRepository.getPostDetail(postId)

                _uiState.update {
                    it.copy(
                        post = post,
                        isLoading = false
                    )
                }

                post.userId?.let { id ->
                    try {
                        val perfil = PetCareRepository.getPerfil(id)
                        _userEmail.value = perfil.email
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}

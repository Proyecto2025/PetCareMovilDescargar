package net.iessochoa.vanesa.petcare.ui.viewModel.post

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import net.iessochoa.vanesa.petcare.data.api.repository.PostRepository
import net.iessochoa.vanesa.petcare.data.api.repository.UbicacionRepository
import net.iessochoa.vanesa.petcare.ui.post.PostUiState
import net.iessochoa.vanesa.petcare.util.Config

class PostViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(PostUiState())
    val uiState: StateFlow<PostUiState> = _uiState

    init {
        //Cargar posts desde la API real
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                val lista = PostRepository.getAllPosts()

                _uiState.update {
                    it.copy(
                        posts = lista,
                        listaFiltrada = lista,
                        isLoading = false //Si fuera true mostraría el circulo de carga
                    )
                }

            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun getAllPosts() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                val lista = PostRepository.getAllPosts()

                _uiState.update {
                    it.copy(
                        posts = lista,
                        listaFiltrada = lista,
                        isLoading = false
                    )
                }

                aplicarFiltros() // para que respete los filtros activos

            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun cambiarFiltro(nuevo: String) {
        _uiState.update { it.copy(filtro = nuevo) }
        aplicarFiltros()
    }

    fun cambiarAnimal(nuevo: String) {
        _uiState.update { it.copy(filtroAnimal = nuevo) }
        aplicarFiltros()
    }

    //Para que se guarde si se cambia un filtro no se pierda el otro
    private fun aplicarFiltros() {
        val lista = _uiState.value.posts

        val ciudadNorm = normalizar(_uiState.value.ciudad)

        val filtrada = lista
            //FILTRO CATEGORÍA
            .filter { post ->
                _uiState.value.filtro == "TODO" ||
                        post.categoria.name == _uiState.value.filtro
            }
            //FILTRO ANIMAL
            .filter { post ->
                _uiState.value.filtroAnimal == "TODO" ||
                        post.tipoAnimal.name == _uiState.value.filtroAnimal
            }
            //FILTRO UBICACIÓN (provincia o municipio)
            .filter { post ->
                if (ciudadNorm.isBlank()) true
                else {
                    val provinciaPost = normalizar(post.ubicacion)
                    val municipioPost = normalizar(post.municipio)

                    provinciaPost.contains(ciudadNorm) ||
                            municipioPost.contains(ciudadNorm)
                }
            }

        _uiState.update { it.copy(listaFiltrada = filtrada) }
    }

    //Sugerencias con Geoapify
    fun actualizarTextoUbicacion(texto: String, context: Context) {
        _uiState.update { it.copy(textoUbicacion = texto) }

        viewModelScope.launch {
            val apiKey = Config.getApiKey(context)

            val sugerencias = UbicacionRepository.sugerenciasGeoapify(texto, apiKey)

            _uiState.update {
                it.copy(sugerenciasUbicacion = sugerencias)
            }
        }
    }

    //Para q encuentre Elche y Alicante (ya q la api busca por (Elx / Elche)
    fun seleccionarUbicacion(ubicacion: String) {
        val limpio = ubicacion
            .replace("/", " ") //Elx / Elche -> Elx  Elche
            .substringAfterLast(" ")   //Elx  Elche -> Elche

        _uiState.update {
            it.copy(
                ciudad = limpio, //guarda “Elche”
                textoUbicacion = "",
                sugerenciasUbicacion = emptyList()
            )
        }

        aplicarFiltros()
    }

    private fun normalizar(texto: String): String {
        return texto
            .lowercase()
            .replace("á", "a")
            .replace("é", "e")
            .replace("í", "i")
            .replace("ó", "o")
            .replace("ú", "u")
            .replace("/", " ")
            .replace("-", " ")
            .trim()
    }

    fun limpiarUbicacion() {
        _uiState.update {
            it.copy(
                ciudad = "",
                textoUbicacion = "",
                sugerenciasUbicacion = emptyList()
            )
        }
        aplicarFiltros()
    }
}
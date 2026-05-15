package net.iessochoa.vanesa.petcare.ui.viewModel.user

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import net.iessochoa.vanesa.petcare.data.api.repository.PetCareRepository
import net.iessochoa.vanesa.petcare.data.api.repository.UbicacionRepository
import net.iessochoa.vanesa.petcare.data.local.UserSessionRepository
import net.iessochoa.vanesa.petcare.data.model.dto.request.advice.CreateAdviceDtoReq
import net.iessochoa.vanesa.petcare.data.model.dto.request.post.CreateAdoptionPostDtoReq
import net.iessochoa.vanesa.petcare.data.model.dto.request.post.CreatePostDtoReq
import net.iessochoa.vanesa.petcare.data.model.enums.AdviceCategory
import net.iessochoa.vanesa.petcare.data.model.enums.PostCategory
import net.iessochoa.vanesa.petcare.data.model.enums.TypeAnimal
import net.iessochoa.vanesa.petcare.data.local.provider.UserIdProvider
import net.iessochoa.vanesa.petcare.ui.user.PublishUiState

class PublishViewModel(
    private val sessionRepository: UserSessionRepository,
    private val userIdProvider: UserIdProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(PublishUiState())
    val uiState: StateFlow<PublishUiState> = _uiState

    //Lógica para la barra de arriba, ver progreso
    fun siguientePaso() {
        if (validarPasoActual()) {
            _uiState.value = _uiState.value.copy(paso = _uiState.value.paso + 1)
        }
    }

    fun pasoAnterior() {
        _uiState.update { state ->
            if (state.paso > 1) state.copy(paso = state.paso - 1)
            else state
        }
    }

    fun cambiarModo(tipoPublicar: String) {
        _uiState.update { it.copy(tipoPublicar = tipoPublicar) }
    }

    fun cambiarTipoPublicacion(tipo: String) {
        _uiState.update { it.copy(tipoPublicacion = tipo) }
    }

    fun cambiarTipoAnimal(animal: String) {
        _uiState.update { it.copy(tipoAnimal = animal) }
    }

    fun actualizarTitulo(texto: String) {
        _uiState.update { it.copy(titulo = texto) }
    }

    //PROVINCIA (Geoapify)
    fun actualizarProvincia(texto: String) {
        _uiState.update { it.copy(textoProvincia = texto) }

        if (texto.length < 3) {
            _uiState.update { it.copy(sugerenciasProvincia = emptyList()) }
            return
        }

        viewModelScope.launch {
            val lista = UbicacionRepository.buscarProvincias(texto)
            _uiState.update { it.copy(sugerenciasProvincia = lista) }
        }
    }

    fun seleccionarProvincia(provincia: String) {
        val limpio = provincia
            .replace("/", " ")
            .substringAfterLast(" ")
            .trim()

        _uiState.update {
            it.copy(
                provincia = limpio,              //limpio: Alicante
                provinciaVisible = provincia,    //visible: Alacant / Alicante
                textoProvincia = "",             //limpia el buscador
                sugerenciasProvincia = emptyList()
            )
        }
    }



    fun seleccionarMunicipio(municipio: String) {
        val limpio = municipio
            .replace("/", " ")
            .substringAfterLast(" ")
            .trim()

        _uiState.update {
            it.copy(
                municipio = limpio,              //limpio: Elche
                municipioVisible = municipio,    //visible: Elx / Elche
                textoMunicipio = "",             //limpia el buscador
                sugerenciasMunicipio = emptyList()
            )
        }
    }

    fun limpiarProvincia() {
        _uiState.update {
            it.copy(
                provincia = "",
                provinciaVisible = "",
                textoProvincia = "",
                sugerenciasProvincia = emptyList(),
            )
        }
    }


    //MUNICIPIO (Geoapify)
    fun actualizarMunicipio(texto: String) {
        _uiState.update { it.copy(textoMunicipio = texto) }

        if (texto.length < 2 || _uiState.value.provinciaVisible.isBlank()) {
            _uiState.update { it.copy(sugerenciasMunicipio = emptyList()) }
            return
        }

        viewModelScope.launch {
            val lista = UbicacionRepository.buscarMunicipios(
                provincia = _uiState.value.provinciaVisible,
                municipio = texto
            )
            _uiState.update { it.copy(sugerenciasMunicipio = lista) }
        }
    }



    fun limpiarMunicipio() {
        _uiState.update {
            it.copy(
                municipio = "",
                municipioVisible = "",
                textoMunicipio = "",
                sugerenciasMunicipio = emptyList()
            )
        }
    }





    //DESCRIPCIONES
    fun actualizarDescripcionCorta(texto: String) {
        _uiState.update { it.copy(descripcionCorta = texto) }
    }

    fun actualizarDescripcionLarga(texto: String) {
        _uiState.update { it.copy(descripcionLarga = texto) }
    }

    fun actualizarDetallesExtra(texto: String) {
        _uiState.update { it.copy(detallesExtra = texto) }
    }

    fun cambiarDonarPertenencias(valor: Boolean) {
        _uiState.update { it.copy(donarPertenencias = valor) }
    }

    fun actualizarPertenencias(texto: String) {
        _uiState.update { it.copy(pertenencias = texto) }
    }


    //CONSEJO
    fun cambiarTipoConsejo(tipo: String) {
        _uiState.update { it.copy(tipoConsejo = tipo) }
    }

    fun actualizarSubtitulo(texto: String) {
        _uiState.update { it.copy(subtitulo = texto) }
    }

    //IMAGEN
    fun actualizarImagen(url: Uri) {
        _uiState.update { it.copy(imagen = url) }
    }

    //VALIDACIÓN
    fun validarPasoActual(): Boolean {
        val state = uiState.value

        return when (state.paso) {

            1 -> {
                if (state.tipoPublicar == "POST") {
                    state.tipoPublicacion.isNotBlank() &&
                            state.tipoAnimal.isNotBlank() &&
                            state.titulo.isNotBlank() &&
                            state.subtitulo.trim().isNotEmpty() &&
                            state.provincia.isNotBlank() &&
                            state.municipio.isNotBlank()
                } else {
                    state.tipoConsejo.isNotBlank() &&
                            state.titulo.isNotBlank() &&
                            state.subtitulo.isNotBlank()
                }
            }

            2 -> {
                state.descripcionCorta.isNotBlank() &&
                        state.descripcionLarga.isNotBlank()
            }

            3 -> true //Opcional

            4 -> {
                if (state.tipoPublicar == "POST" && state.tipoPublicacion == "ADOPCION") {
                    if (state.donarPertenencias == null) return false
                    if (state.donarPertenencias == true)
                        state.pertenencias.isNotBlank()
                    else true
                } else true
            }

            5 -> state.imagen != null

            else -> true
        }
    }


    fun publicar(context: Context) {

        val state = uiState.value

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                val userId = userIdProvider.getUserId()
                if (userId == null) {
                    _uiState.update { it.copy(isLoading = false, error = "No se pudo obtener el usuario") }
                    return@launch
                }

                val imageUri = state.imagen ?: return@launch

                if (state.tipoPublicar == "POST") {
                    if (state.tipoPublicacion == "ADOPCION") {

                        val dto = CreateAdoptionPostDtoReq(
                            postCategory = PostCategory.ADOPCION,
                            typeAnimal = TypeAnimal.valueOf(state.tipoAnimal),
                            title = state.titulo,
                            subtitle = state.subtitulo,
                            shortDescription = state.descripcionCorta,
                            longDescription = state.descripcionLarga,
                            extraDetails = state.detallesExtra,
                            belongings = if (state.donarPertenencias == true) //Para q se quede como una lista
                                state.pertenencias.split(",").map { it.trim() }
                            else null,
                            location = state.provincia,
                            municipality = state.municipio,
                            userId = userId
                        )

                        PetCareRepository.createAdoption(context, dto, imageUri)

                    } else {

                        val dto = CreatePostDtoReq(
                            postCategory = PostCategory.valueOf(state.tipoPublicacion),
                            typeAnimal = TypeAnimal.valueOf(state.tipoAnimal),
                            title = state.titulo,
                            subtitle = state.subtitulo,
                            shortDescription = state.descripcionCorta,
                            longDescription = state.descripcionLarga,
                            extraDetails = state.detallesExtra,
                            location = state.provincia,
                            municipality = state.municipio,
                            userId = userId


                        )


                        PetCareRepository.createPost(context, dto, imageUri)
                    }

                } else {

                    val dto = CreateAdviceDtoReq(
                        category = AdviceCategory.valueOf(state.tipoConsejo),
                        title = state.titulo,
                        subtitle = state.subtitulo,
                        shortDescription = state.descripcionCorta,
                        longDescription = state.descripcionLarga,
                        extraDetail = state.detallesExtra,
                        userId = userId
                    )

                    PetCareRepository.createAdvice(context, dto, imageUri)
                }

                _uiState.update { it.copy(isLoading = false, publicado = true) }

            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message ?: "Error desconocido") }            }
        }
    }




}
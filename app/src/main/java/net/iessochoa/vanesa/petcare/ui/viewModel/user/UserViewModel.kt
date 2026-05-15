package net.iessochoa.vanesa.petcare.ui.viewModel.user

import android.app.Activity
import android.content.Context
import kotlinx.coroutines.channels.Channel
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import net.iessochoa.vanesa.petcare.data.api.repository.PetCareRepository
import net.iessochoa.vanesa.petcare.data.local.UserSessionRepository
import net.iessochoa.vanesa.petcare.data.model.dto.request.user.CreateUserDtoReq
import net.iessochoa.vanesa.petcare.data.model.dto.request.user.EditPasswordDtoReq
import net.iessochoa.vanesa.petcare.data.model.dto.request.user.EditUserDtoReq
import net.iessochoa.vanesa.petcare.data.model.dto.request.user.LoginUserDtoReq
import net.iessochoa.vanesa.petcare.data.model.dto.user.CreateUserDto
import net.iessochoa.vanesa.petcare.data.model.dto.user.LoginUserDto
import net.iessochoa.vanesa.petcare.data.model.dto.user.PerfilDto
import net.iessochoa.vanesa.petcare.data.model.dto.user.PerfilFiltradoDto
import net.iessochoa.vanesa.petcare.data.local.provider.UserIdProvider
import net.iessochoa.vanesa.petcare.model.CategoriaPost
import net.iessochoa.vanesa.petcare.ui.PetCareUiState
import net.iessochoa.vanesa.petcare.ui.RequestStatus
import net.iessochoa.vanesa.petcare.ui.user.LoginUiState
import net.iessochoa.vanesa.petcare.ui.user.ProfileUiState
import net.iessochoa.vanesa.petcare.ui.user.RegisterUiState

class UserViewModel(
    private val sessionRepository: UserSessionRepository
) : ViewModel() {
    //Para el nav
    val isLoggedIn = sessionRepository.isLoggedIn
    val sessionUserId = sessionRepository.userId


    private val _uiState = MutableStateFlow(PetCareUiState())
    val uiState: StateFlow<PetCareUiState> = _uiState

    //Navegar a términos
    private val _navigationEvent = MutableStateFlow<String?>(null)
    val navigationEvent: StateFlow<String?> = _navigationEvent

    fun navegarATerminos() {
        _navigationEvent.value = "terminos"
    }

    fun resetNavigation() {
        _navigationEvent.value = null
    }

    // LOGIN
    private val _loginStatus = MutableStateFlow<RequestStatus<LoginUserDto>>(RequestStatus.Idle)
    // (BORRAR) val loginStatus: StateFlow<RequestStatus<LoginUserDto>> = _loginStatus

    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState: StateFlow<LoginUiState> = _loginUiState


    fun actualizarUsuario(value: String) {
        _loginUiState.update { it.copy(usuario = value.trim(), errorUsuario = null) }
    }

    fun actualizarContrasenya(value: String) {
        _loginUiState.update { it.copy(contrasenya = value.trim(), errorContrasenya = null) }
    }

    fun actualizarRecordarSesion(value: Boolean) {
        _loginUiState.update { it.copy(recordarSesion = value) }
    }

    //Para q se guarde el inicio de sesión (si marca o no el checkBox)
    fun resetLoginExitoso() {
        _loginUiState.update { it.copy(loginExitoso = false) }
    }


    fun login() {
        val state = _loginUiState.value

        //LIMPIAR ERRORES ANTERIORES
        _loginUiState.update {
            it.copy(
                errorUsuario = null,
                errorContrasenya = null
            )
        }

        if (!_aceptaTerminos.value) {
            _loginUiState.update { it.copy(errorContrasenya = "Debes aceptar los términos y condiciones") }
            return
        }

        if (state.usuario.isBlank()) {
            _loginUiState.update { it.copy(errorUsuario = "El usuario no puede estar vacío") }
            return
        }

        if (state.contrasenya.isBlank()) {
            _loginUiState.update { it.copy(errorContrasenya = "La contraseña no puede estar vacía") }
            return
        }

        //Para q salga el botón cargando
        _loginUiState.update { it.copy(isLoading = true) }
        _loginStatus.value = RequestStatus.Loading

        viewModelScope.launch {

            // Intentar login
            val response = try {
                PetCareRepository.login(
                    LoginUserDtoReq(
                        userName = state.usuario,
                        password = state.contrasenya
                    )
                )
            } catch (e: Exception) {
                val msg = e.message ?: "Error desconocido"
                _loginStatus.value = RequestStatus.Error(msg)
                _loginUiState.update { it.copy(errorContrasenya = msg, isLoading = false) }
                return@launch
            }

            // SI EL LOGIN FALLA -> response = null
            if (response == null) {
                _loginStatus.value = RequestStatus.Error("Usuario o contraseña incorrectos")

                //DESACTIVAR LOADING
                _loginUiState.update {
                    it.copy(
                        errorContrasenya = "Usuario o contraseña incorrectos",
                        isLoading = false
                    )
                }
                return@launch
            }

            //Solo la guarda si le da a mantener sesión
            if (state.recordarSesion) {
                sessionRepository.setLoggedIn()
            }


            //Guardar SIEMPRE el ID en memoria temporal
            sessionRepository.setTempUserId(response.id)

            //Guardar en DataStore solo si marca recordar sesión
            if (state.recordarSesion) {
                sessionRepository.saveUserId(response.id)
            }



            _loginStatus.value = RequestStatus.Success(response)

            _loginUiState.update {
                it.copy(
                    userId = response.id,
                    loginExitoso = true,
                    isLoading = false
                )
            }
        }
    }



    fun mostrarError(mensaje: String) {
        _loginUiState.update { it.copy(errorContrasenya = mensaje) }
    }

    // REGISTRO
    private val _registerUiState = MutableStateFlow(RegisterUiState())
    val registerUiState: StateFlow<RegisterUiState> = _registerUiState

    private val _registerStatus = MutableStateFlow<RequestStatus<CreateUserDto>>(RequestStatus.Idle)
    val registerStatus: StateFlow<RequestStatus<CreateUserDto>> = _registerStatus

    fun actualizarRecordarSesionRegistro(value: Boolean) {
        _registerUiState.update { it.copy(recordarSesion = value) }
    }

    fun onNombreChanged(v: String) = _registerUiState.update { it.copy(nombre = v, errorNombre = null) }
    fun onUsuarioChanged(v: String) = _registerUiState.update { it.copy(usuario = v.trim(), errorUsuario = null) }
    fun onTelefonoChanged(v: String) = _registerUiState.update { it.copy(telefono = v.trim(), errorTelefono = null) }
    fun onCorreoChanged(v: String) = _registerUiState.update { it.copy(correo = v.trim(), errorCorreo = null) }
    fun onContrasenyaChanged(v: String) = _registerUiState.update { it.copy(contrasenya = v.trim(), errorContrasenya = null) }
    fun onConfirmarChanged(v: String) = _registerUiState.update { it.copy(confirmar = v.trim(), errorConfirmar = null) }

    //Validaciones
    private fun esCorreoValido(email: String): Boolean {
        val regex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
        return regex.matches(email)
    }

    private fun esTelefonoValido(telefono: String): Boolean {
        return telefono.length == 9 && telefono.all { it.isDigit() }
    }

    private fun esUserNameValido(user: String): Boolean {
        val regex = Regex("^[A-Za-z0-9_.-]{3,20}$")
        return regex.matches(user)
    }

    fun registerUser() {
        val state = _registerUiState.value

        //LIMPIAR ERRORES ANTERIORES
        _registerUiState.update {
            it.copy(
                errorNombre = null,
                errorUsuario = null,
                errorTelefono = null,
                errorCorreo = null,
                errorContrasenya = null,
                errorConfirmar = null
            )
        }

        if (!_aceptaTerminos.value) {
            _registerUiState.update { it.copy(errorConfirmar = "Debes aceptar los términos y condiciones") }
            return
        }

        if (state.nombre.isBlank()) { _registerUiState.update { it.copy(errorNombre = "El nombre no puede estar vacío") }; return }
        if (state.usuario.isBlank()) { _registerUiState.update { it.copy(errorUsuario = "El usuario no puede estar vacío") }; return }

        //VALIDAR USERNAME LOCAL
        if (!esUserNameValido(state.usuario)) {
            _registerUiState.update { it.copy(errorUsuario = "Nombre de usuario inválido") }
            return
        }

        if (state.telefono.isBlank()) { _registerUiState.update { it.copy(errorTelefono = "El teléfono no puede estar vacío") }; return }

        //VALIDAR TELÉFONO
        if (!esTelefonoValido(state.telefono)) {
            _registerUiState.update { it.copy(errorTelefono = "Teléfono inválido") }
            return
        }

        if (state.correo.isBlank()) { _registerUiState.update { it.copy(errorCorreo = "El correo no puede estar vacío") }; return }

        //VALIDAR EMAIL
        if (!esCorreoValido(state.correo)) {
            _registerUiState.update { it.copy(errorCorreo = "Correo inválido") }
            return
        }

        if (state.contrasenya.isBlank()) { _registerUiState.update { it.copy(errorContrasenya = "La contraseña no puede estar vacía") }; return }
        if (state.confirmar.isBlank()) { _registerUiState.update { it.copy(errorConfirmar = "Debes confirmar la contraseña") }; return }
        if (state.contrasenya != state.confirmar) { _registerUiState.update { it.copy(errorConfirmar = "Las contraseñas no coinciden") }; return }


        _registerUiState.update { it.copy(isLoading = true)}
        _registerStatus.value = RequestStatus.Loading

        viewModelScope.launch {

            //VALIDAR USERNAME EN LA API
            val existe = PetCareRepository.existsUserName(state.usuario)
            try {
                if (existe) {
                    _registerUiState.update { it.copy(errorUsuario = "El usuario ya existe", isLoading = false) }
                    _registerStatus.value = RequestStatus.Idle
                    return@launch
                }
            } catch (e: Exception) {
                //Si falla la API, no bloqueamos el registro
            }

            try {
                val response = PetCareRepository.createUser(
                    CreateUserDtoReq(
                        fullName = state.nombre,
                        userName = state.usuario,
                        phoneNumber = state.telefono,
                        email = state.correo,
                        password = state.contrasenya,
                        confirmPassword = state.confirmar
                    )
                )

                //Si la respuesta es inválida
                if (!response.isSuccessful) {
                    when {
                        response.code() == 409 ->
                            _registerUiState.update { it.copy(errorConfirmar = "El correo o teléfono ya está registrado") }

                        existe ->
                            _registerUiState.update { it.copy(errorUsuario = "El usuario ya existe") }

                        else ->
                            _registerUiState.update { it.copy(errorConfirmar = "Error desconocido") }


                    }

                    _registerStatus.value = RequestStatus.Error("Error en registro")
                    _registerUiState.update { it.copy(isLoading = false) }

                    return@launch
                }

                //Si la respuesta es correcta se guarda
                val body = response.body()
                if (body == null) {
                    _registerStatus.value = RequestStatus.Error("Respuesta inválida del servidor")
                    _registerUiState.update { it.copy(isLoading = false) }
                    return@launch
                }


                _registerStatus.value = RequestStatus.Success(body)

                //Marcar como logueado SOLO si marca recordar sesión
                if (state.recordarSesion) {
                    sessionRepository.setLoggedIn()
                }


                //Guardar SIEMPRE el ID en memoria temporal
                sessionRepository.setTempUserId(body.id)

                //Guardar en DataStore solo si marca recordar sesión
                if (state.recordarSesion) {
                    sessionRepository.saveUserId(body.id)
                }

                //El usuario queda logeado tras registrarse
                _loginUiState.update { it.copy(loginExitoso = true, userId = body.id) }

                _registerUiState.update { it.copy(isLoading = false) }

            } catch (e: Exception) {   //CATCH GENÉRICO
                val msg = e.message ?: "Error desconocido"
                _registerStatus.value = RequestStatus.Error(msg)
                _registerUiState.update { it.copy(errorConfirmar = msg, isLoading = false) }
                return@launch
            }
        }
    }


    // PERFIL
    private val _perfilStatus = MutableStateFlow<RequestStatus<PerfilDto>>(RequestStatus.Idle)
    val perfilStatus: StateFlow<RequestStatus<PerfilDto>> = _perfilStatus

    private val _perfilFiltrado =
        MutableStateFlow<RequestStatus<PerfilFiltradoDto>>(RequestStatus.Idle)
    val perfilFiltrado: StateFlow<RequestStatus<PerfilFiltradoDto>> = _perfilFiltrado

    private val _perfilUiState = MutableStateFlow(ProfileUiState())
    val perfilUiState: StateFlow<ProfileUiState> = _perfilUiState


    val userIdProvider = UserIdProvider(sessionRepository)

    fun initPerfil() {
        viewModelScope.launch {
            val id = userIdProvider.getUserId() ?: return@launch


            //Spinner
            _perfilUiState.update { it.copy(isLoadingPerfil = true) }
            try {
                val perfil = PetCareRepository.getPerfil(id)

                _perfilUiState.update {
                    it.copy(
                        user = perfil,
                        isLoadingPerfil = false
                    )
                }

                if (perfilUiState.value.filtro == null) {
                    cambiarFiltroPerfil(CategoriaPost.ADOPCION)
                }


            } catch (e: Exception) {
                _perfilUiState.update { it.copy(isLoadingPerfil = false) }
            }
        }
    }

    fun cambiarFiltroPerfil(categoria: CategoriaPost) {
        viewModelScope.launch {

            val id = userIdProvider.getUserId() ?: return@launch

            try {
                _perfilUiState.update {
                    it.copy(
                        filtro = categoria,
                        isLoadingLista = true
                    )
                }


                val type = when (categoria) {
                    CategoriaPost.CONSEJO -> "advice"
                    else -> categoria.name
                }

                val filtrado = PetCareRepository.getPerfilFiltrado(id, type)

                if (categoria == CategoriaPost.CONSEJO) {
                    _perfilUiState.update {
                        it.copy(
                            posts = emptyList(),
                            advices = filtrado.advices ?: emptyList(),
                            isLoadingLista = false
                        )
                    }
                } else {
                    _perfilUiState.update {
                        it.copy(
                            posts = filtrado.posts ?: emptyList(),
                            advices = emptyList(),
                            isLoadingLista = false
                        )
                    }
                }

            } catch (e: Exception) {
                _perfilUiState.update { it.copy(isLoadingLista = false) }
            }
        }
    }


    fun cargarPerfil(id: Long) {
        _perfilStatus.value = RequestStatus.Loading

        viewModelScope.launch {
            try {
                val response = PetCareRepository.getPerfil(id)
                _perfilStatus.value = RequestStatus.Success(response)
            } catch (e: Exception) {
                _perfilStatus.value = RequestStatus.Error("Error al cargar perfil")
            }
        }
    }





    fun cargarPerfilFiltrado(id: Long, type: String) {
        _perfilFiltrado.value = RequestStatus.Loading

        viewModelScope.launch {
            try {
                val response = PetCareRepository.getPerfilFiltrado(id, type)
                _perfilFiltrado.value = RequestStatus.Success(response)
            } catch (e: Exception) {
                _perfilFiltrado.value = RequestStatus.Error("Error al filtrar perfil")
            }
        }
    }

    //Borrar post (suspended pq la api tarda unos cuantos ms), para q se vea q se ha borrado
    suspend fun borrarPost(id: Long) {
        PetCareRepository.deletePost(id)
    }

    //Borrar advice
    suspend fun borrarAdvice(id: Long) {
        PetCareRepository.deleteAdvice(id)
    }

    //Borrar cuenta
    fun eliminarCuenta(id: Long, context: Context) {
        viewModelScope.launch {
            try {
                PetCareRepository.deleteUser(id)
                cerrarSesion(context)
            } catch (e: Exception) { }
        }
    }


    //Cerrar sesión
    fun cerrarSesion(context: Context? = null) {
        viewModelScope.launch {

            //Limpiar DataStore (isLoggedIn = false, userId = -1)
            sessionRepository.clearSession()

            //Resetear login COMPLETO (incluye userId = -1)
            _loginUiState.value = LoginUiState(
                loginExitoso = false,
                userId = -1L
            )

            //Resetear el perfil
            _perfilUiState.value = ProfileUiState()

            //Resetear estados globales
            _uiState.value = PetCareUiState()

            _aceptaTerminos.value = false

            //Forzar recomposición total
            (context as? Activity)?.recreate()
        }
    }




    //Editar perfil
    fun editarPerfil(
        id: Long,
        fullName: String,
        userName: String,
        phoneNumber: String,
        email: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _perfilUiState.update { it.copy(isUpdating = true) }
            println("BOTÓN EDITAR PERFIL PULSADO")

            try {
                //VALIDACIONES ANTES DE LLAMAR A LA API
                if (fullName.isBlank()) {
                    _events.send(UserEvent.FieldError("fullName", "El nombre no puede estar vacío"))
                    _perfilUiState.update { it.copy(isUpdating = false) }
                    return@launch
                }

                if (userName.isBlank()) {
                    _events.send(UserEvent.FieldError("userName", "El nombre de usuario no puede estar vacío"))
                    _perfilUiState.update { it.copy(isUpdating = false) }
                    return@launch
                }

                //Validación username (local)
                val regexUser = Regex("^[A-Za-z0-9_.-]{3,20}$")
                if (!regexUser.matches(userName)) {
                    _events.send(UserEvent.FieldError("userName", "Nombre de usuario inválido"))
                    _perfilUiState.update { it.copy(isUpdating = false) }
                    return@launch
                }

                //Validación teléfono
                val regexPhone = Regex("^\\d{9}\$")
                if (!regexPhone.matches(phoneNumber)) {
                    _events.send(UserEvent.FieldError("phoneNumber", "Número de teléfono inválido"))
                    _perfilUiState.update { it.copy(isUpdating = false) }
                    return@launch
                }

                //Validación email
                val regexEmail = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
                if (!regexEmail.matches(email)) {
                    _events.send(UserEvent.FieldError("email", "Correo inválido"))
                    _perfilUiState.update { it.copy(isUpdating = false) }
                    return@launch
                }

                //VALIDAR USERNAME ÚNICO EN LA API
                try {
                    println("LLAMADA API EDIT PERFIL")

                    val existe = PetCareRepository.existsUserName(userName)
                    val oldUser = PetCareRepository.getPerfil(id)

                    //Si existe y no es el mismo usuario actual
                    if (existe && oldUser.userName != userName) {
                        _perfilUiState.update { it.copy(isUpdating = false) }
                        _events.send(UserEvent.FieldError("userName", "Ese nombre de usuario ya existe"))
                        return@launch
                    }

                } catch (e: Exception) {
                    //Si falla la API, no bloqueo
                    println("ERROR EDIT PERFIL")

                }

                //Llamar a la API
                val dto = EditUserDtoReq(
                    fullName = fullName,
                    userName = userName,
                    phoneNumber = phoneNumber,
                    email = email
                )

                val oldUser = PetCareRepository.getPerfil(id)

                PetCareRepository.editUser(id, dto)

                val newUser = PetCareRepository.getPerfil(id)

                //Detectar cambios
                val cambios = mutableListOf<String>()
                if (oldUser.fullName != newUser.fullName) cambios.add("Nombre")
                if (oldUser.userName != newUser.userName) cambios.add("Nombre de usuario")
                if (oldUser.phoneNumber != newUser.phoneNumber) cambios.add("Número de teléfono")
                if (oldUser.email != newUser.email) cambios.add("Correo")

                _events.send(UserEvent.ProfileUpdated(cambios))

                _perfilUiState.update { it.copy(isUpdating = false) }
                _perfilStatus.value = RequestStatus.Idle

                onSuccess()
                println("FIN EDIT PERFIL")
            } catch (e: Exception) {
                println("ERROR EDIT PERFIL")
                _perfilUiState.update { it.copy(isUpdating = false) }

                val msg = e.message ?: ""

                when {
                    msg.contains("usuario", true) ->
                        _events.send(UserEvent.FieldError("userName", "Ese nombre de usuario ya existe"))

                    msg.contains("correo", true) || msg.contains("email", true) ->
                        _events.send(UserEvent.FieldError("email", "Ese correo ya está registrado"))

                    msg.contains("tel", true) ->
                        _events.send(UserEvent.FieldError("phoneNumber", "Ese teléfono ya está registrado"))

                    else ->
                        _events.send(UserEvent.Error("Error al actualizar perfil"))
                }
            }
        }
    }



    sealed class UserEvent {
        data class ProfileUpdated(val cambios: List<String>) : UserEvent()
        object PasswordChanged : UserEvent() //Evento exito
        data class Error(val message: String) : UserEvent()
        data class FieldError(val field: String, val message: String) : UserEvent() //Al extraer el nombre de la api
    }

    //Canal para enviar eventos a la UI
    private val _events = Channel<UserEvent>()
    val events = _events.receiveAsFlow()




    data class PasswordUiState(
        val isLoading: Boolean = false
    )

    private val _passwordUiState = MutableStateFlow(PasswordUiState())
    val passwordUiState: StateFlow<PasswordUiState> = _passwordUiState


    //Editar solo contraseña
    fun cambiarPassword(
        id: Long,
        password: String,
        confirmPassword: String,
    ) {
        println("ENTRANDO EN cambiarPassword")
        viewModelScope.launch {
            _passwordUiState.value = PasswordUiState(isLoading = true)
            try {

                val dto = EditPasswordDtoReq(
                    password = password,
                    confirmPassword = confirmPassword
                )

                println("LLAMANDO A LA API")
                //Llama a la api
                PetCareRepository.editPassword(id, dto)

                _passwordUiState.value = PasswordUiState(isLoading = false)

                //Notifica que ha ido bien
                _events.send(UserEvent.PasswordChanged)

                println("FIN DE CAMBIARPASSWORD")
            } catch (e: Exception) {
                println("ERROR cambiarPass")
                _passwordUiState.value = PasswordUiState(isLoading = false)

                //Detectar si el backend devolvió "La nueva contraseña no puede ser igual a la anterior"
                val errorMsg = when {
                    e.message?.contains("igual", ignoreCase = true) == true ->
                        "La nueva contraseña no puede ser igual a la anterior"

                    e.message?.contains("same", ignoreCase = true) == true ->
                        "La nueva contraseña no puede ser igual a la anterior"

                    else ->
                        "Error al cambiar contraseña"
                }

                _events.send(UserEvent.Error(errorMsg))
            }

        }
    }


    //Términos y condiciones
    private val _aceptaTerminos = mutableStateOf(false)
    val aceptaTerminos: State<Boolean> = _aceptaTerminos

    fun actualizarAceptaTerminos(valor: Boolean) {
        _aceptaTerminos.value = valor
    }
}

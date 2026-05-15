package net.iessochoa.vanesa.petcare.ui.user

data class RegisterUiState(
    val nombre: String = "",
    val usuario: String = "",
    val telefono: String = "",
    val correo: String = "",
    val contrasenya: String = "",
    val confirmar: String = "",

    //Errores por cada campo
    val errorNombre: String? = null,
    val errorUsuario: String? = null,
    val errorTelefono: String? = null,
    val errorCorreo: String? = null,
    val errorContrasenya: String? = null,
    val errorConfirmar: String? = null,

    //Términos
    val errorGeneral: String? = null,

    //Recordar la sesión
    val recordarSesion: Boolean = false,
    val isLoading: Boolean = false


)
package net.iessochoa.vanesa.petcare.ui.user

//Se usa en el userViewModel
data class LoginUiState(
    val userId: Long = -1,
    val usuario: String = "",
    val contrasenya: String = "",
    val loginExitoso: Boolean = false, //el login fue válido y puedo navegar a Post

    //Errores por cada campo
    val errorUsuario: String? = null,
    val errorContrasenya: String? = null,

    val recordarSesion: Boolean = false,
    val isLoading: Boolean = false
)
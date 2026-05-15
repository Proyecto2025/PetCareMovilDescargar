package net.iessochoa.vanesa.petcare.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import net.iessochoa.vanesa.petcare.ui.RequestStatus
import net.iessochoa.vanesa.petcare.ui.components.ProfileField
import net.iessochoa.vanesa.petcare.ui.components.rememberKeyboardState
import net.iessochoa.vanesa.petcare.ui.components.ErrorText
import net.iessochoa.vanesa.petcare.ui.viewModel.user.UserViewModel
import net.iessochoa.vanesa.petcare.R
import net.iessochoa.vanesa.petcare.ui.components.ErrorSnackbar

@Composable
fun PersonalDataScreen(
    viewModel: UserViewModel,
    onBack: (Boolean) -> Unit
) {

    val perfilStatus by viewModel.perfilStatus.collectAsState()

    //Estado global del ViewModel (loading al actualizar)
    val uiState by viewModel.perfilUiState.collectAsState()

    val realUserId by produceState<Long?>(initialValue = null) {
        value = viewModel.userIdProvider.getUserId()
    }

    val snackbarHostState = remember { SnackbarHostState() }

    //Errores accesibles por campo
    var errorNombre by remember { mutableStateOf<String?>(null) }
    var errorUsuario by remember { mutableStateOf<String?>(null) }
    var errorTelefono by remember { mutableStateOf<String?>(null) }
    var errorCorreo by remember { mutableStateOf<String?>(null) }


    val errorNombreVacio = stringResource(R.string.perfil_error_nombre_vacio)
    val errorUsuarioVacio = stringResource(R.string.perfil_error_usuario_vacio)
    val errorTelefonoInvalido = stringResource(R.string.perfil_error_telefono_invalido)
    val errorCorreoInvalido = stringResource(R.string.perfil_error_correo_invalido)

    LaunchedEffect(realUserId) {
        if (realUserId != null) {
            viewModel.cargarPerfil(realUserId!!)
        }
    }

    //Escuchar eventos del ViewModel
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {

                is UserViewModel.UserEvent.Error -> {
                    //Error global
                    snackbarHostState.showSnackbar(event.message)
                }

                is UserViewModel.UserEvent.FieldError -> {
                    when (event.field) {
                        "userName" -> errorUsuario = event.message
                    }
                }

                is UserViewModel.UserEvent.ProfileUpdated -> {
                    onBack(true)   //vuelve al perfil
                }

                else -> {}
            }
        }
    }

    //Manejo de estados
    when (perfilStatus) {

        //Mostrar spinner mientras carga el perfil
        is RequestStatus.Idle,
        is RequestStatus.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF0C653D))
            }
            return
        }

        //Error = mostrar mensaje
        is RequestStatus.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(stringResource(R.string.perfil_error_cargar), color = Color.Red)
            }
            return
        }

        //Datos cargados
        is RequestStatus.Success -> {

            val user = (perfilStatus as RequestStatus.Success).data

            //Variables locales para editar los campos
            var nombre by remember { mutableStateOf(user.fullName) }
            var usuario by remember { mutableStateOf(user.userName) }
            var telefono by remember { mutableStateOf(user.phoneNumber) }
            var correo by remember { mutableStateOf(user.email) }

            //Saber si el teclado está abierto
            val isKeyboardOpen by rememberKeyboardState()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
                    //Para q no se quede una cosa blanca al abrir el teclado
                    .padding(bottom = if (isKeyboardOpen) 250.dp else 0.dp)
            ) {

                //Snackbar manual (el del error)
                ErrorSnackbar(snackbarHostState)

                //Volver
                Text(
                    text = stringResource(R.string.perfil_volver),
                    modifier = Modifier
                        .clickable { onBack(false) }   //Volver sin cambios
                        .padding(bottom = 20.dp),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF0C653D),
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = stringResource(R.string.perfil_titulo),
                    style = MaterialTheme.typography.titleLarge,
                    color = Color(0xFF0C653D),
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(20.dp))

                ProfileField(
                    label = stringResource(R.string.perfil_nombre),
                    value = nombre,
                    onValueChange = {
                        nombre = it
                        errorNombre = null
                    }
                )
                if (errorNombre != null) ErrorText(errorNombre!!)

                ProfileField(
                    label = stringResource(R.string.perfil_usuario),
                    value = usuario,
                    onValueChange = {
                        usuario = it
                        errorUsuario = null
                    }
                )
                if (errorUsuario != null) ErrorText(errorUsuario!!)

                ProfileField(
                    label = stringResource(R.string.perfil_telefono),
                    value = telefono,
                    onValueChange = {
                        telefono = it
                        errorTelefono = null
                    }
                )
                if (errorTelefono != null) ErrorText(errorTelefono!!)

                ProfileField(
                    label = stringResource(R.string.perfil_correo),
                    value = correo,
                    onValueChange = {
                        correo = it
                        errorCorreo = null
                    }
                )
                if (errorCorreo != null) ErrorText(errorCorreo!!)

                Spacer(Modifier.height(30.dp))

                //Confirmar
                Button(
                    onClick = {

                        //VALIDACIONES ACCESIBLES
                        errorNombre = if (nombre.isBlank()) errorNombreVacio else null
                        errorUsuario = if (usuario.isBlank()) errorUsuarioVacio else null
                        errorTelefono = if (!telefono.matches(Regex("^\\d{9}\$"))) errorTelefonoInvalido else null
                        errorCorreo = if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) errorCorreoInvalido else null


                        //Si hay errores no continua
                        if (errorNombre != null ||
                            errorUsuario != null ||
                            errorTelefono != null ||
                            errorCorreo != null
                        ) return@Button

                        //Llamar al ViewModel
                        viewModel.editarPerfil(
                            id = realUserId ?: -1,
                            fullName = nombre,
                            userName = usuario,
                            phoneNumber = telefono,
                            email = correo
                        ) {
                            onBack(true)   //Volver indicando que hubo cambios
                        }
                    },
                    enabled = !uiState.isUpdating,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0C653D),
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                ) {
                    if (uiState.isUpdating) {
                        CircularProgressIndicator(
                            color = Color.White,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(20.dp)
                        )
                    } else {
                        Text(stringResource(R.string.perfil_confirmar), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

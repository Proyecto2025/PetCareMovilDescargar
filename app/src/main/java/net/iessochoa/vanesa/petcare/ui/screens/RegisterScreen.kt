package net.iessochoa.vanesa.petcare.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import net.iessochoa.vanesa.petcare.R
import net.iessochoa.vanesa.petcare.ui.RequestStatus
import net.iessochoa.vanesa.petcare.ui.components.ErrorText
import net.iessochoa.vanesa.petcare.ui.components.InputField
import net.iessochoa.vanesa.petcare.ui.viewModel.user.UserViewModel

@Composable
fun RegisterScreen(
    viewModel: UserViewModel,
    onRegisterSuccess: () -> Unit,
    onNavigateToTerms: () -> Unit
) {
    val uiState by viewModel.registerUiState.collectAsState()
    val registerStatus by viewModel.registerStatus.collectAsState()

    //Para el teclado del móvil
    val focusManager = LocalFocusManager.current

    //Para los términos
    val aceptaTerminos by viewModel.aceptaTerminos

    var errorTerminos by remember { mutableStateOf<String?>(null) }

    val errorAceptarTerminos = stringResource(R.string.error_aceptar_terminos)

    //Detectar si el registro fue correcto
    LaunchedEffect(registerStatus) {
        if (registerStatus is RequestStatus.Success) {
            onRegisterSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .imePadding(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            stringResource(R.string.register_titulo),
            color = Color(0xFF0C653D),
            style = MaterialTheme.typography.displaySmall,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        InputField(
            value = uiState.nombre,
            onValueChange = { viewModel.onNombreChanged(it) },
            label = stringResource(R.string.register_nombre_completo),
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
        )
        uiState.errorNombre?.let { ErrorText(it) }

        Spacer(modifier = Modifier.height(20.dp))

        InputField(
            value = uiState.usuario,
            onValueChange = { viewModel.onUsuarioChanged(it) },
            label = stringResource(R.string.register_usuario),
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
        )
        uiState.errorUsuario?.let { ErrorText(it) }

        Spacer(modifier = Modifier.height(20.dp))

        InputField(
            value = uiState.telefono,
            onValueChange = { viewModel.onTelefonoChanged(it) },
            label = stringResource(R.string.register_telefono),
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
        )
        uiState.errorTelefono?.let { ErrorText(it) }

        Spacer(modifier = Modifier.height(20.dp))

        InputField(
            value = uiState.correo,
            onValueChange = { viewModel.onCorreoChanged(it) },
            label = stringResource(R.string.register_correo),
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
        )
        uiState.errorCorreo?.let { ErrorText(it) }

        Spacer(modifier = Modifier.height(20.dp))

        InputField(
            value = uiState.contrasenya,
            onValueChange = { viewModel.onContrasenyaChanged(it) },
            label = stringResource(R.string.register_contrasena),
            isPassword = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
        )
        uiState.errorContrasenya?.let { ErrorText(it) }

        Spacer(modifier = Modifier.height(20.dp))

        InputField(
            value = uiState.confirmar,
            onValueChange = { viewModel.onConfirmarChanged(it) },
            label = stringResource(R.string.register_confirmar_contrasena),
            isPassword = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
        )
        uiState.errorConfirmar?.let { ErrorText(it) }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = uiState.recordarSesion,
                onCheckedChange = { viewModel.actualizarRecordarSesionRegistro(it) }
            )
            Text(
                stringResource(R.string.register_mantener_sesion),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        //Aceptar términos
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = aceptaTerminos,
                onCheckedChange = { viewModel.actualizarAceptaTerminos(it) }
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    stringResource(R.string.login_aceptar_terminos),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF0C653D),
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable { onNavigateToTerms() }
                )
            }
        }

        //Mostrar error debajo del checkbox
        errorTerminos?.let { ErrorText(it) }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                if (aceptaTerminos) {
                    errorTerminos = null
                    viewModel.registerUser()
                } else {
                    errorTerminos = errorAceptarTerminos
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF0C653D),
                contentColor = Color.White
            )
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Text(stringResource(R.string.register_boton))
            }
        }
    }
}

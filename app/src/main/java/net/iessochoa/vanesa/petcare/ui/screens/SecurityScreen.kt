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
import net.iessochoa.vanesa.petcare.R
import net.iessochoa.vanesa.petcare.ui.components.ErrorSnackbar
import net.iessochoa.vanesa.petcare.ui.components.ErrorText
import net.iessochoa.vanesa.petcare.ui.components.LoadingScreen
import net.iessochoa.vanesa.petcare.ui.components.ProfileField
import net.iessochoa.vanesa.petcare.ui.components.rememberKeyboardState
import net.iessochoa.vanesa.petcare.ui.viewModel.user.UserViewModel

@Composable
fun SecurityScreen(
    viewModel: UserViewModel,
    onBack: (Boolean) -> Unit
) {
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    //Snackbar para mostrar "El error"
    val snackbarHostState = remember { SnackbarHostState() }

    // Saber si el teclado está abierto
    val isKeyboardOpen by rememberKeyboardState()

    //Spinner y bloqueo botón
    val passwordUiState by viewModel.passwordUiState.collectAsState()

    //Ver si las contraseñas son iguales y hay algo escrito
    val passwordsMatch = password.isNotEmpty() &&
            confirmPassword.isNotEmpty() &&
            password == confirmPassword

    //El id real (para q no salga -1)
    val realUserId by produceState<Long?>(initialValue = null) {
        value = viewModel.userIdProvider.getUserId()
    }

    //Strings
    val txtVolver = stringResource(R.string.perfil_volver)
    val txtTitulo = stringResource(R.string.perfil_seguridad)
    val txtCambiar = stringResource(R.string.security_cambiar_contrasena)
    val txtConfirmar = stringResource(R.string.security_confirmar_contrasena)
    val txtErrorNoCoinciden = stringResource(R.string.security_error_no_coinciden)
    val txtBotonConfirmar = stringResource(R.string.security_boton_confirmar)

    //Escuchar eventos del ViewModel
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is UserViewModel.UserEvent.PasswordChanged -> {
                    onBack(true)
                }

                is UserViewModel.UserEvent.Error -> {
                    snackbarHostState.showSnackbar(event.message)
                }

                else -> {}
            }
        }
    }

    if (passwordUiState.isLoading) {
        LoadingScreen()
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
            .padding(bottom = if (isKeyboardOpen) 250.dp else 0.dp)
    ) {

        // Snackbar manual (el del error)
        ErrorSnackbar(snackbarHostState)

        //Volver
        Text(
            text = txtVolver,
            modifier = Modifier
                .clickable { onBack(false) }
                .padding(bottom = 20.dp),
            style = MaterialTheme.typography.titleMedium,
            color = Color(0xFF0C653D),
            fontWeight = FontWeight.Bold
        )

        Text(
            text = txtTitulo,
            style = MaterialTheme.typography.titleLarge,
            color = Color(0xFF0C653D),
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(20.dp))

        ProfileField(
            label = txtCambiar,
            value = password,
            onValueChange = { password = it },
            isPassword = true
        )

        ProfileField(
            label = txtConfirmar,
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            isPassword = true
        )

        if (!passwordsMatch && confirmPassword.isNotEmpty()) {
            ErrorText(txtErrorNoCoinciden)
        }

        Spacer(Modifier.height(30.dp))

        //Confirmar
        Button(
            onClick = {
                viewModel.cambiarPassword(
                    id = realUserId ?: -1,
                    password = password,
                    confirmPassword = confirmPassword
                )
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF0C653D),
                contentColor = Color.White
            ),
            enabled = passwordsMatch && !passwordUiState.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (passwordUiState.isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Text(txtBotonConfirmar)
            }
        }
    }
}

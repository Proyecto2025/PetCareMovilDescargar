package net.iessochoa.vanesa.petcare.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
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
import net.iessochoa.vanesa.petcare.ui.components.ErrorText
import net.iessochoa.vanesa.petcare.ui.components.InputField
import net.iessochoa.vanesa.petcare.ui.viewModel.user.UserViewModel
import net.iessochoa.vanesa.petcare.R

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: UserViewModel,
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToTerms: () -> Unit
) {
    val uiState by viewModel.loginUiState.collectAsState()

    val aceptaTerminos by viewModel.aceptaTerminos

    val errorTerminos = stringResource(R.string.error_aceptar_terminos)

    val focusManager = LocalFocusManager.current

    val navigationEvent by viewModel.navigationEvent.collectAsState()

    LaunchedEffect(navigationEvent) {
        if (navigationEvent == "terminos") {
            onNavigateToTerms()
            viewModel.resetNavigation()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = stringResource(R.string.login_titulo),
            color = Color(0xFF0C653D),
            style = MaterialTheme.typography.displaySmall,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        InputField(
            value = uiState.usuario,
            onValueChange = { viewModel.actualizarUsuario(it) },
            label = stringResource(R.string.login_usuario),
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
        )
        //Mostrar los errores debajo del label
        uiState.errorUsuario?.let { ErrorText(it) }

        Spacer(modifier = Modifier.height(20.dp))

        InputField(
            value = uiState.contrasenya,
            onValueChange = { viewModel.actualizarContrasenya(it) },
            label = stringResource(R.string.login_contrasena),
            modifier = Modifier.fillMaxWidth(),
            isPassword = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
        )

        uiState.errorContrasenya?.let { ErrorText(it) }

        Spacer(modifier = Modifier.height(16.dp))

        //Checkbox de mantener sesión iniciada
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 2.dp)
        ) {
            Checkbox(
                checked = uiState.recordarSesion,
                onCheckedChange = { viewModel.actualizarRecordarSesion(it) }
            )


            Text(
                stringResource(R.string.login_mantener_sesion),
                style = MaterialTheme.typography.bodyMedium


            )


        }

        //Checkbox de términos y condiciones
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 2.dp)
        ) {
            Checkbox(
                checked = aceptaTerminos,
                onCheckedChange = { viewModel.actualizarAceptaTerminos(it) }
            )

            Text(
                stringResource(R.string.login_aceptar_terminos),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.clickable {
                    viewModel.navegarATerminos()
                },
                color = Color(0xFF0C653D),
                textDecoration = TextDecoration.Underline
            )
        }


        Button(
            onClick = {
                if (aceptaTerminos) {
                    viewModel.login()
                } else {
                    viewModel.mostrarError(errorTerminos)
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF0C653D),
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Text(stringResource(R.string.login_ingresar))
            }
        }

        //Para que no pete al pasar a la otra pantalla y para que salga la barra de abajo
        LaunchedEffect(uiState.loginExitoso) {
            if (uiState.loginExitoso) {
                onLoginSuccess()
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(
            onClick = onNavigateToRegister,
            colors = ButtonDefaults.textButtonColors(
                contentColor = Color(0xFF0C653D)
            )
        ) {
            Row {
                Text(stringResource(R.string.login_no_cuenta))
                Text(
                    stringResource(R.string.login_crea_una),
                    textDecoration = TextDecoration.Underline
                )

            }
        }
    }
}

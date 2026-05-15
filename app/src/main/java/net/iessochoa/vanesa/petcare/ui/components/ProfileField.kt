package net.iessochoa.vanesa.petcare.ui.components

import android.R.attr.singleLine
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun ProfileField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    //Opcionales
    isPassword: Boolean = false, //Si es uno cantraseña, se ocultará
    readOnly: Boolean = false,
    isError: Boolean = false,
    errorMessage: String? = null
) {
    // Estado para mostrar/ocultar la contraseña
    val visible = remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {

        Text(
            text = label,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            singleLine = true,
            readOnly = readOnly,
            isError = isError,
            //El visual transformation es para q al escribir la contraseña no se "vea"
            visualTransformation =
                if (isPassword && !visible.value) PasswordVisualTransformation()
                else VisualTransformation.None,
            //Para poder poner el icono al final del field
            trailingIcon = {
                if (isPassword) {
                    IconButton(onClick = { visible.value = !visible.value }) {
                        Icon(
                            imageVector = if (visible.value) Icons.Filled.Visibility
                            else Icons.Filled.VisibilityOff,
                            contentDescription = null
                        )
                    }
                }
            }
        )

        if (isError && errorMessage != null) {
            Text( text = errorMessage,
                color = Color.Red,
                modifier = Modifier.padding(start = 4.dp, bottom = 12.dp)
            )
        }
    }
}

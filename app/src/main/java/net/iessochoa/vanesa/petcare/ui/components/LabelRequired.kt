package net.iessochoa.vanesa.petcare.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color

@Composable
fun LabelRequired(texto: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = texto,
            color = Color(0xFF0C653D), //Verde
            style = MaterialTheme.typography.labelLarge
        )
        Text(
            text = " *",
            color = Color.Red, //Rojo
            style = MaterialTheme.typography.labelLarge
        )
    }
}

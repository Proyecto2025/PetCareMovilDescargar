package net.iessochoa.vanesa.petcare.ui.components

import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

/**
 * Según si estamos en advice o Post se muestra un menú u otro
 */
@Composable
fun DropDown(
    opciones: List<String>,
    selected: String,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
    fontSize: Int = 22,
    enabled: Boolean = true,
    textStyle: TextStyle = LocalTextStyle.current
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        Text(
            text = "$selected ⌄",
            style = textStyle,
            color = Color(0xFF0C653D),
            fontSize = fontSize.sp,
            modifier = Modifier
                .padding(8.dp)
                .clickable(enabled = enabled) { expanded = true }
        )

        DropdownMenu(
            expanded = expanded && enabled,
            onDismissRequest = { expanded = false }
        ) {
            opciones.forEach { opcion ->
                DropdownMenuItem(
                    text = { Text(opcion) },
                    onClick = {
                        onSelect(opcion)
                        expanded = false
                    }
                )
            }
        }
    }
}


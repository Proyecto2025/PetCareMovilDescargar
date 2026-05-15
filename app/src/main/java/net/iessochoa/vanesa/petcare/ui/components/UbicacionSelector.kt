package net.iessochoa.vanesa.petcare.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import net.iessochoa.vanesa.petcare.R

@Composable
fun UbicacionSelector(
    value: String,                 // ubicación seleccionada (provinciaVisible o municipioVisible)
    textoBusqueda: String,         // lo que escribe el usuario
    sugerencias: List<String>,
    onValueChange: (String) -> Unit,
    onSelect: (String) -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    var abierto by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = stringResource(R.string.item_ubicacion),
                tint = Color(0xFF0C653D),
                modifier = Modifier
                    .size(22.dp)
                    .clickable { abierto = !abierto }
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Mostrar ubicación seleccionada
            Text(
                text = if (value.isBlank()) stringResource(R.string.ubicacion_sin) else value,
                modifier = Modifier.padding(end = 4.dp)
            )

            // La X para limpiar
            if (value.isNotBlank()) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.ubicacion_limpiar),
                    tint = Color.Gray,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { onClear() }
                )
            }
        }

        if (abierto) {

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = textoBusqueda,
                onValueChange = onValueChange,
                label = { Text(stringResource(R.string.ubicacion_buscar)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                trailingIcon = {
                    if (textoBusqueda.isNotBlank()) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(R.string.ubicacion_limpiar),
                            tint = Color.Gray,
                            modifier = Modifier
                                .size(20.dp)
                                .clickable { onClear() }
                        )
                    }
                }
            )

            if (sugerencias.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 200.dp)
                ) {
                    items(sugerencias) { item ->
                        Text(
                            text = item,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                                .clickable {
                                    onSelect(item)   // Guarda la ubicación final
                                    abierto = false  // Cierra el menú
                                }
                        )
                    }
                }
            }
        }
    }
}

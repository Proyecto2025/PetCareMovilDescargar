package net.iessochoa.vanesa.petcare.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import net.iessochoa.vanesa.petcare.R
import net.iessochoa.vanesa.petcare.ui.advice.DetailUI

@Composable
fun DetailItem(data: DetailUI) {

    var loadError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(16.dp),
    ) {

        Text(
            text = data.titulo,
            style = MaterialTheme.typography.titleLarge
        )

        data.ubicacion?.let {
            Text(
                text = stringResource(R.string.detail_ubicacion, it),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }

        if (loadError) {
            //Título si la imagen falla
            Text(
                text = data.titulo,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )
        } else {
            AsyncImage(
                model = data.imagen,
                contentDescription = data.subtitulo,  //ALT
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                onError = {
                    loadError = true
                }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = data.descripcionLarga,
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (data.datosExtra.isNotBlank()) {
            Text(
                text = stringResource(R.string.detail_datos_extra),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = data.datosExtra,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        data.pertenencias?.takeIf { it.isNotEmpty() }?.let {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.detail_pertenencias),
                style = MaterialTheme.typography.titleMedium
            )
            it.forEach { item ->
                Text("• $item", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

package net.iessochoa.vanesa.petcare.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import net.iessochoa.vanesa.petcare.R
import net.iessochoa.vanesa.petcare.data.model.dto.user.PerfilPostAndAdviceDto

/**
 * Tarjeta específica para el perfil del usuario
 * PerfilPostAndAdviceDto
 */
@Composable
fun ProfileItemCard(
    item: PerfilPostAndAdviceDto,
    onClick: () -> Unit,
    onDelete: (() -> Unit)? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable { onClick() },
        shape = MaterialTheme.shapes.medium
    ) {

        Box {

            Column(modifier = Modifier.padding(16.dp)) {

                //Nombre del usuario
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(Modifier.height(8.dp))

                //Imagen
                AsyncImage(
                    model = item.image,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                )

                Spacer(Modifier.height(8.dp))

                //Descripción corta
                Text(
                    text = item.shortDescription,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            //Botón eliminar
            if (onDelete != null) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.perfil_dialogo_eliminar_confirmar),
                    tint = Color.Red,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .size(28.dp)
                        .clickable { onDelete() }
                )
            }
        }
    }
}

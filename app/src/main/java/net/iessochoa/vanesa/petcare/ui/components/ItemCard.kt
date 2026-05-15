package net.iessochoa.vanesa.petcare.ui.components

import android.R.attr.contentDescription
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import net.iessochoa.vanesa.petcare.R
import net.iessochoa.vanesa.petcare.model.CategoriaPost
import net.iessochoa.vanesa.petcare.model.Post
import net.iessochoa.vanesa.petcare.model.TipoAnimal
import net.iessochoa.vanesa.petcare.model.common.ItemVisualData

/**
 * Tarjeta según si es Advice o Post
 * Se llama a la interfaz porque alli es donde estan los datos
 */
@Composable
fun ItemCard(
    item: ItemVisualData,
    onClick: () -> Unit,
    onDelete: (() -> Unit)? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable { onClick() }, //Se pueda hacer click
        shape = MaterialTheme.shapes.medium //Esquinas medio redondas
    ) {

        Box {

            Column(modifier = Modifier.padding(16.dp)) {

                Text(
                    text = item.uiNombreUsuario,
                    style = MaterialTheme.typography.titleMedium,
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.uiCategoriaTexto,
                        color = Color(0xFF0C653D),
                        style = MaterialTheme.typography.labelMedium
                    )

                    //Solo si tiene ubicación
                    item.uiUbicacion?.let { ubicacion ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = stringResource(R.string.item_ubicacion),
                                tint = Color(0xFF0C653D),
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = ubicacion,
                                color = Color(0xFF0C653D),
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }
                    }
                }

                AsyncImage(
                    model = item.uiImagen,
                    contentDescription = item.uiSubtitulo,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                )

                Text(
                    text = item.uiTitulo,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                Text(
                    text = item.uiDescripcionCorta,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

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



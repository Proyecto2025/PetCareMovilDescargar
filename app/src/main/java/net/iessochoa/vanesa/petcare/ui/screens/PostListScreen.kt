package net.iessochoa.vanesa.petcare.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import net.iessochoa.vanesa.petcare.model.Post
import net.iessochoa.vanesa.petcare.ui.components.DropDown
import net.iessochoa.vanesa.petcare.ui.components.FilterList
import net.iessochoa.vanesa.petcare.ui.components.ItemCard
import net.iessochoa.vanesa.petcare.ui.components.UbicacionSelector
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.platform.LocalContext
import net.iessochoa.vanesa.petcare.ui.components.LoadingScreen
import net.iessochoa.vanesa.petcare.ui.viewModel.post.PostViewModel
import net.iessochoa.vanesa.petcare.R

@Composable
fun PostListScreen(
    modifier: Modifier = Modifier,
    viewModel: PostViewModel = viewModel(),
    onPostSelected: (Post) -> Unit
) {

    //Se vuelva a cargar (al publicar salga el nuevo post)
    LaunchedEffect(Unit) {
        viewModel.getAllPosts()
    }

    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = stringResource(R.string.postlist_titulo),
            color = Color(0xFF0C653D),
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        Column(modifier = Modifier.fillMaxWidth()) {

            //FILTRO CATEGORÍA
            DropDown(
                opciones = listOf(
                    stringResource(R.string.categoria_todo),
                    stringResource(R.string.categoria_adopcion),
                    stringResource(R.string.categoria_ayuda),
                    stringResource(R.string.categoria_extravio)
                ),
                selected = uiState.filtro,
                onSelect = { viewModel.cambiarFiltro(it) },
                modifier = Modifier.widthIn(min = 130.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier.fillMaxWidth()
            ) {

                //Selector de ubicación
                UbicacionSelector(
                    value = uiState.ciudad,  //Ubicación seleccionada
                    textoBusqueda = uiState.textoUbicacion, //Lo que escribe el usuario
                    sugerencias = uiState.sugerenciasUbicacion,
                    onValueChange = { viewModel.actualizarTextoUbicacion(it, context) },
                    onSelect = { seleccion ->
                        viewModel.seleccionarUbicacion(seleccion)
                    },
                    onClear = { viewModel.limpiarUbicacion() },
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(12.dp))

                //FILTRO ANIMAL
                FilterList(
                    opciones = listOf(
                        stringResource(R.string.categoria_todo),
                        stringResource(R.string.animal_perro),
                        stringResource(R.string.animal_gato)
                    ),
                    onSelect = { viewModel.cambiarAnimal(it) },
                    modifier = Modifier.size(40.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        //LISTA DE POSTS
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(bottom = 90.dp)
        ) {

            //SPINNER DE CARGA
            if (uiState.isLoading) {
                LoadingScreen()
            } else {

                //LISTA VACÍA
                if (uiState.listaFiltrada.isEmpty()) {
                    Text(
                        text = stringResource(R.string.postlist_vacio),
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.align(Alignment.TopCenter)
                    )
                } else {

                    //LISTA DE POSTS
                    LazyColumn {
                        items(uiState.listaFiltrada) { post ->
                            ItemCard(
                                item = post,
                                onClick = { onPostSelected(post) }
                            )
                        }
                    }
                }
            }
        }
    }
}

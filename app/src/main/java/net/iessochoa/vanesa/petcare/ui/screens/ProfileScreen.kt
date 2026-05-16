package net.iessochoa.vanesa.petcare.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.launch
import net.iessochoa.vanesa.petcare.R
import net.iessochoa.vanesa.petcare.data.model.dto.user.PerfilPostAndAdviceDto
import net.iessochoa.vanesa.petcare.model.CategoriaPost
import net.iessochoa.vanesa.petcare.ui.components.*
import net.iessochoa.vanesa.petcare.ui.viewModel.user.UserViewModel

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: UserViewModel,
    passwordChanged: Boolean = false, //Flag
    dataChanged: Boolean = false, //Flag
    onPostSelected: (PerfilPostAndAdviceDto) -> Unit,
    onAdviceSelected: (PerfilPostAndAdviceDto) -> Unit,
    onCambiarDatos: () -> Unit,
    onSeguridad: () -> Unit,
    onEliminarCuenta: () -> Unit,
    onDrawerStateChange: (Boolean) -> Unit //Para el bottomBar
) {

    val uiState by viewModel.perfilUiState.collectAsState()
    val context = LocalContext.current

    //Snackbar
    val snackbarHostState = remember { SnackbarHostState() }

    //Para evitar que se repita el snackbar al volver atrás
    var passwordSnackbarShown by remember { mutableStateOf(false) }
    var dataSnackbarShown by remember { mutableStateOf(false) }

    //Menu lateral, como en ig
    var isDrawerOpen by remember { mutableStateOf(false) }

    //Para el circulo de loading (al borrar)
    var isDeleting by remember { mutableStateOf(false) }

    //Para lanzar corrutinas desde el composable (no se pete)
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        onDrawerStateChange(false) //La bottom bar vuelve
    }

    Box(Modifier.fillMaxSize()) {

        //Snackbar siempre visible y personalizado
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 50.dp)
                .zIndex(9999f)
        ) { data ->

            Snackbar(
                shape = RoundedCornerShape(16.dp),
                containerColor = Color(0xFF0C653D).copy(alpha = 0.92f),
                contentColor = Color.White,
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = data.visuals.message,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

    //El menu como en ig
    RightDrawer(
        isOpen = isDrawerOpen,
        onClose = {
            isDrawerOpen = false
            onDrawerStateChange(false)
        },
        drawerContent = {
            Column(Modifier.padding(16.dp)) {

                Text(
                    stringResource(R.string.perfil_seguridad),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            isDrawerOpen = false
                            onSeguridad()
                        }
                        .padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    stringResource(R.string.perfil_cambiar_datos),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            isDrawerOpen = false
                            onCambiarDatos()
                        }
                        .padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    stringResource(R.string.perfil_cerrar_sesion),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            isDrawerOpen = false
                            viewModel.cerrarSesion(context)
                        }
                        .padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Red
                )

                Text(
                    stringResource(R.string.perfil_eliminar_cuenta),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            isDrawerOpen = false
                            viewModel.eliminarCuenta(uiState.user?.id ?: -1, context)
                            onEliminarCuenta()
                        }
                        .padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Red
                )
            }
        }
    ) {

        Scaffold(
            containerColor = Color.Transparent, //Para que el snackbar no quede tapado
        ) { paddingValues ->

            //Mostrar snackbar si viene de Seguridad
            LaunchedEffect(passwordChanged) {
                if (passwordChanged && !passwordSnackbarShown) {
                    passwordSnackbarShown = true
                    snackbarHostState.showSnackbar("Contraseña cambiada")
                }
            }

            //Mostrar snackbar si viene de Datos Personales
            LaunchedEffect(dataChanged) {
                if (dataChanged && !dataSnackbarShown) {
                    dataSnackbarShown = true
                    snackbarHostState.showSnackbar("Datos actualizados")
                }
            }

            //Si está cargando el perfil
            val user = uiState.user
            if (user == null) {
                LoadingScreen()
                return@Scaffold
            }

            val filtro = uiState.filtro

            //Posts de la API según el filtro
            val posts =
                if (filtro == CategoriaPost.CONSEJO) uiState.advices
                else uiState.posts

            //Para poder poner el num de posts y de advices
            val totalPosts = user.numberOfPosts
            val totalAdvices = user.numberOfAdvices

            //API devuelve la lista filtrada
            val listaFiltrada = posts

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Text(
                        text = stringResource(R.string.perfil_titulo_pantalla),
                        color = Color(0xFF0C653D),
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold
                    )

                    IconButton(
                        onClick = {
                            isDrawerOpen = !isDrawerOpen
                            onDrawerStateChange(isDrawerOpen)
                        }
                    ) {
                        Icon(Icons.Default.Menu, contentDescription = "Menú")
                    }
                }

                Column(modifier = Modifier.padding(16.dp)) {

                    Text(
                        text = user.userName,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Text(stringResource(R.string.perfil_num_posts, totalPosts))
                        Text(stringResource(R.string.perfil_num_advices, totalAdvices))
                    }

                    Spacer(Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {

                        PerfilCategoriaIconos(
                            icon = Icons.Default.Pets,
                            categoria = CategoriaPost.ADOPCION,
                            selected = filtro,
                            onClick = { categoria ->
                                categoria?.let { viewModel.cambiarFiltroPerfil(it) }
                            }
                        )

                        PerfilCategoriaIconos(
                            icon = Icons.Default.Search,
                            categoria = CategoriaPost.EXTRAVIO,
                            selected = filtro,
                            onClick = { categoria ->
                                categoria?.let { viewModel.cambiarFiltroPerfil(it) }
                            }
                        )

                        PerfilCategoriaIconos(
                            icon = Icons.Default.VolunteerActivism,
                            categoria = CategoriaPost.AYUDA,
                            selected = filtro,
                            onClick = { categoria ->
                                categoria?.let { viewModel.cambiarFiltroPerfil(it) }
                            }
                        )

                        PerfilConsejoIconos(
                            icon = Icons.Default.ChatBubble,
                            categoria = CategoriaPost.CONSEJO,
                            selected = filtro,
                            onClick = { categoria ->
                                categoria?.let { viewModel.cambiarFiltroPerfil(it) }
                            }
                        )
                    }

                    //Mensaje dinámico según categoría
                    val mensajeVacio = when (filtro) {
                        CategoriaPost.ADOPCION -> stringResource(R.string.perfil_vacio_adopcion)
                        CategoriaPost.EXTRAVIO -> stringResource(R.string.perfil_vacio_extravio)
                        CategoriaPost.AYUDA -> stringResource(R.string.perfil_vacio_ayuda)
                        CategoriaPost.CONSEJO -> stringResource(R.string.perfil_vacio_consejo)
                        else -> stringResource(R.string.perfil_vacio_publicaciones)
                    }

                    //Loading al borrar o cambiar filtro
                    if (uiState.isLoadingLista || isDeleting) {

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Color(0xFF0C653D))
                        }

                    } else {

                        //Si no hay nada, sale el mensaje
                        if (listaFiltrada.isEmpty()) {
                            Text(
                                text = mensajeVacio,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 32.dp),
                                color = Color.Gray,
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center
                            )
                        } else {

                            LazyColumn (
                                contentPadding = PaddingValues(bottom = 90.dp)
                            ) {
                                items(listaFiltrada) { item ->

                                    var mostrarDialogo by remember { mutableStateOf(false) }
                                    val esConsejo = (filtro == CategoriaPost.CONSEJO)

                                    ProfileItemCard(
                                        item = item,
                                        onClick = {
                                            if (esConsejo)
                                                onAdviceSelected(item)
                                            else
                                                onPostSelected(item)
                                        },
                                        onDelete = {
                                            mostrarDialogo = true
                                        }
                                    )

                                    if (mostrarDialogo) {
                                        AlertDialog(
                                            onDismissRequest = { mostrarDialogo = false },
                                            title = { Text(stringResource(R.string.perfil_dialogo_eliminar_titulo)) },
                                            text = { Text(stringResource(R.string.perfil_dialogo_eliminar_texto)) },
                                            confirmButton = {
                                                TextButton(
                                                    onClick = {
                                                        mostrarDialogo = false
                                                        isDeleting = true

                                                        coroutineScope.launch {

                                                            if (filtro == CategoriaPost.CONSEJO) {
                                                                viewModel.borrarAdvice(item.id)
                                                            } else {
                                                                viewModel.borrarPost(item.id)
                                                            }

                                                            viewModel.initPerfil()

                                                            filtro?.let { categoria ->
                                                                viewModel.cambiarFiltroPerfil(categoria)
                                                            }

                                                            isDeleting = false
                                                        }
                                                    }
                                                ) {
                                                    Text(
                                                        stringResource(R.string.perfil_dialogo_eliminar_confirmar),
                                                        color = Color.Red
                                                    )
                                                }
                                            },
                                            dismissButton = {
                                                TextButton(onClick = { mostrarDialogo = false }) {
                                                    Text(stringResource(R.string.perfil_dialogo_eliminar_cancelar))
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

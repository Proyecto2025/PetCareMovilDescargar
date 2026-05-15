package net.iessochoa.vanesa.petcare.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import net.iessochoa.vanesa.petcare.R
import net.iessochoa.vanesa.petcare.data.local.provider.UserSessionRepositoryProvider
import net.iessochoa.vanesa.petcare.ui.user.PublishUiState
import net.iessochoa.vanesa.petcare.ui.viewModel.publish.PublishViewModelFactory
import net.iessochoa.vanesa.petcare.ui.components.DropDown
import net.iessochoa.vanesa.petcare.ui.components.LabelRequired
import net.iessochoa.vanesa.petcare.ui.components.rememberKeyboardState
import net.iessochoa.vanesa.petcare.ui.viewModel.user.PublishViewModel

@Composable
fun PublishScreen(
    onBack: () -> Unit,
    onPublishSuccess: () -> Unit
) {

    val context = LocalContext.current
    val sessionRepository = UserSessionRepositoryProvider.get(context)

    val viewModel: PublishViewModel = viewModel(
        factory = PublishViewModelFactory(sessionRepository)
    )

    val uiState = viewModel.uiState.collectAsState().value
    val isKeyboardOpen by rememberKeyboardState()

    //Si ya se publicó navega
    if (uiState.publicado) {
        onPublishSuccess()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .imePadding()
    ) {

        //Tipo de publicación
        DropDown(
            opciones = listOf(
                stringResource(R.string.publish_titulo_post),
                stringResource(R.string.publish_titulo_consejo)
            ),
            selected = uiState.tipoPublicar,
            onSelect = { viewModel.cambiarModo(it) },
            enabled = uiState.paso == 1,
            textStyle = MaterialTheme.typography.displaySmall.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0C653D)
            )
        )

        Spacer(Modifier.height(12.dp))

        //Barra de progreso
        LinearProgressIndicator(
            progress = uiState.paso / 5f,
            modifier = Modifier.fillMaxWidth(),
            color = Color(0xFF0C653D)
        )

        //Error pero con código (413, 500 ... )
        if (uiState.error != null) {
            Text(
                text = uiState.error!!,
                color = Color.Red,
                modifier = Modifier.padding(8.dp)
            )
        }

        Spacer(Modifier.height(20.dp))

        when (uiState.paso) {
            1 -> Paso1(viewModel, uiState)
            2 -> Paso2(viewModel, uiState)
            3 -> Paso3(viewModel, uiState)
            4 -> Paso4(viewModel, uiState)
            5 -> Paso5(viewModel, uiState)
        }

        Spacer(Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            //Atrás o cancelar
            if (uiState.paso > 1) {
                Button(
                    onClick = { viewModel.pasoAnterior() },
                    enabled = !uiState.isLoading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0C653D),
                        contentColor = Color.White
                    )
                ) {
                    Text(stringResource(R.string.publish_atras))
                }
            } else {
                Button(
                    onClick = onBack,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0C653D),
                        contentColor = Color.White
                    )
                ) {
                    Text(stringResource(R.string.publish_cancelar))
                }
            }

            //Siguiente o publicar
            Button(
                onClick = {
                    if (uiState.paso == 5) {
                        viewModel.publicar(context)
                    } else {
                        viewModel.siguientePaso()
                    }
                },
                enabled = viewModel.validarPasoActual() && !uiState.isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0C653D),
                    contentColor = Color.White
                )
            ) {
                if (uiState.isLoading && uiState.paso == 5) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Text(
                        if (uiState.paso == 5)
                            stringResource(R.string.publish_publicar)
                        else
                            stringResource(R.string.publish_siguiente)
                    )
                }
            }
        }
    }
}

@Composable
fun Paso1(
    viewModel: PublishViewModel,
    uiState: PublishUiState
) {

    //Límites del backend
    val maxTitle = 60
    val maxSubtitle = 60

    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {

        if (uiState.tipoPublicar == "POST") {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Column(modifier = Modifier.weight(1f)) {
                    LabelRequired(stringResource(R.string.publish_tipo_publicacion))
                    DropDown(
                        opciones = listOf("ADOPCION", "AYUDA", "EXTRAVIO"),
                        selected = uiState.tipoPublicacion,
                        onSelect = { viewModel.cambiarTipoPublicacion(it) },
                        modifier = Modifier.width(150.dp),
                        fontSize = 16
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    LabelRequired(stringResource(R.string.publish_tipo_animal))
                    DropDown(
                        opciones = listOf("PERRO", "GATO"),
                        selected = uiState.tipoAnimal,
                        onSelect = { viewModel.cambiarTipoAnimal(it) },
                        modifier = Modifier.width(150.dp),
                        fontSize = 16
                    )
                }
            }

            //TÍTULO POST
            LabelRequired(stringResource(R.string.publish_titulo_post_label))
            TextField(
                value = uiState.titulo,
                onValueChange = { if (it.length <= maxTitle) viewModel.actualizarTitulo(it) },
                placeholder = { Text(stringResource(R.string.publish_titulo_post_placeholder)) },
                modifier = Modifier.fillMaxWidth(),
                supportingText = {
                    Text("${uiState.titulo.length}/$maxTitle")
                }
            )


            //PROVINCIA
            LabelRequired(stringResource(R.string.publish_provincia))
            Column {
                val provinciaText =
                    if (uiState.provinciaVisible.isNotBlank() && uiState.textoProvincia.isBlank())
                        uiState.provinciaVisible
                    else uiState.textoProvincia

                TextField(
                    value = provinciaText,
                    onValueChange = { viewModel.actualizarProvincia(it) },
                    placeholder = { Text(stringResource(R.string.publish_provincia_placeholder)) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = uiState.provinciaVisible.isBlank(),
                    trailingIcon = {
                        if (uiState.provinciaVisible.isNotBlank()) {
                            IconButton(onClick = { viewModel.limpiarProvincia() }) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                )

                uiState.sugerenciasProvincia.forEach { provincia ->
                    Text(
                        text = provincia,
                        color = Color(0xFF0C653D),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.seleccionarProvincia(provincia) }
                            .padding(8.dp)
                    )
                }
            }

            //MUNICIPIO
            LabelRequired(stringResource(R.string.publish_municipio))
            Column {
                val municipioText =
                    if (uiState.municipioVisible.isNotBlank() && uiState.textoMunicipio.isBlank())
                        uiState.municipioVisible
                    else uiState.textoMunicipio

                TextField(
                    value = municipioText,
                    onValueChange = { viewModel.actualizarMunicipio(it) },
                    placeholder = { Text(stringResource(R.string.publish_municipio_placeholder)) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = uiState.municipioVisible.isBlank(),
                    trailingIcon = {
                        if (uiState.municipioVisible.isNotBlank()) {
                            IconButton(onClick = { viewModel.limpiarMunicipio() }) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                )

                uiState.sugerenciasMunicipio.forEach { municipio ->
                    Text(
                        text = municipio,
                        color = Color.Gray,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.seleccionarMunicipio(municipio) }
                            .padding(8.dp)
                    )
                }
            }

            //SUBTÍTULO
            LabelRequired(stringResource(R.string.publish_subtitulo))
            TextField(
                value = uiState.subtitulo,
                onValueChange = { if (it.length <= maxSubtitle) viewModel.actualizarSubtitulo(it) },
                placeholder = { Text(stringResource(R.string.publish_subtitulo_placeholder)) },
                modifier = Modifier.fillMaxWidth(),
                supportingText = {
                    Text("${uiState.subtitulo.length}/$maxSubtitle")
                }
            )
        }

        //CONSEJO
        if (uiState.tipoPublicar == "CONSEJO") {

            LabelRequired(stringResource(R.string.publish_tipo_consejo))
            DropDown(
                opciones = listOf("COMIDA", "HIGIENE", "ACCESORIOS"),
                selected = uiState.tipoConsejo,
                onSelect = { viewModel.cambiarTipoConsejo(it) },
                modifier = Modifier.width(150.dp),
                fontSize = 16
            )

            //TÍTULO CONSEJO
            LabelRequired(stringResource(R.string.publish_titulo_consejo_label))
            TextField(
                value = uiState.titulo,
                onValueChange = { if (it.length <= maxTitle) viewModel.actualizarTitulo(it) },
                placeholder = { Text(stringResource(R.string.publish_titulo_consejo_placeholder)) },
                modifier = Modifier.fillMaxWidth(),
                supportingText = {
                    Text("${uiState.titulo.length}/$maxTitle")
                }
            )

            //SUBTÍTULO CONSEJO
            LabelRequired(stringResource(R.string.publish_subtitulo))
            TextField(
                value = uiState.subtitulo,
                onValueChange = { if (it.length <= maxSubtitle) viewModel.actualizarSubtitulo(it) },
                placeholder = { Text(stringResource(R.string.publish_subtitulo_placeholder)) },
                modifier = Modifier.fillMaxWidth(),
                supportingText = {
                    Text("${uiState.subtitulo.length}/$maxSubtitle")
                }
            )
        }
    }
}

@Composable
fun Paso2(
    viewModel: PublishViewModel,
    uiState: PublishUiState
) {

    //Límites
    val maxShort = 60
    val maxLong = 1500

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

        //DESCRIPCIÓN CORTA
        LabelRequired(stringResource(R.string.publish_descripcion_corta))
        TextField(
            value = uiState.descripcionCorta,
            onValueChange = { if (it.length <= maxShort) viewModel.actualizarDescripcionCorta(it) },
            modifier = Modifier.fillMaxWidth(),
            supportingText = {
                Text("${uiState.descripcionCorta.length}/$maxShort")
            }
        )

        //DESCRIPCIÓN LARGA
        LabelRequired(stringResource(R.string.publish_descripcion_larga))
        TextField(
            value = uiState.descripcionLarga,
            onValueChange = { if (it.length <= maxLong) viewModel.actualizarDescripcionLarga(it) },
            modifier = Modifier.fillMaxWidth(),
            minLines = 5,
            supportingText = {
                Text("${uiState.descripcionLarga.length}/$maxLong")
            }
        )
    }
}


@Composable
fun Paso3(
    viewModel: PublishViewModel,
    uiState: PublishUiState
) {

    //Límite
    val maxExtra = 500

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

        Text(
            stringResource(R.string.publish_detalles_extra_titulo),
            color = Color(0xFF0C653D)
        )

        TextField(
            value = uiState.detallesExtra,
            onValueChange = { if (it.length <= maxExtra) viewModel.actualizarDetallesExtra(it) },
            label = { Text(stringResource(R.string.publish_detalles_extra_label)) },
            modifier = Modifier.fillMaxWidth(),
            minLines = 4,
            supportingText = {
                Text("${uiState.detallesExtra.length}/$maxExtra")
            }
        )
    }
}


@Composable
fun Paso4(
    viewModel: PublishViewModel,
    uiState: PublishUiState
) {

    //Si no aplica (solo ADOPCIÓN)
    if (!(uiState.tipoPublicar == "POST" && uiState.tipoPublicacion == "ADOPCION")) {
        Text(stringResource(R.string.publish_no_aplica))
        return
    }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

        LabelRequired(stringResource(R.string.publish_donar_pertenencias))

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {

            //OPCIÓN SÍ
            Row(
                modifier = Modifier.clickable { viewModel.cambiarDonarPertenencias(true) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = uiState.donarPertenencias == true,
                    onClick = { viewModel.cambiarDonarPertenencias(true) }
                )
                Text(stringResource(R.string.publish_si))
            }

            //OPCIÓN NO
            Row(
                modifier = Modifier.clickable { viewModel.cambiarDonarPertenencias(false) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = uiState.donarPertenencias == false,
                    onClick = { viewModel.cambiarDonarPertenencias(false) }
                )
                Text(stringResource(R.string.publish_no))
            }
        }

        //Campo de pertenencias solo si eligió "Sí"
        if (uiState.donarPertenencias == true) {
            LabelRequired(stringResource(R.string.publish_pertenencias_label))
            TextField(
                value = uiState.pertenencias,
                onValueChange = { viewModel.actualizarPertenencias(it) },
                placeholder = { Text(stringResource(R.string.publish_pertenencias_placeholder)) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}


@Composable
fun Paso5(
    viewModel: PublishViewModel,
    uiState: PublishUiState
) {

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.actualizarImagen(it) }
    }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

        LabelRequired(stringResource(R.string.publish_seleccionar_imagen))

        uiState.imagen?.let { uri ->
            AsyncImage(
                model = uri,
                contentDescription = uiState.subtitulo,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                contentScale = ContentScale.Crop
            )
        } ?: run {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .background(Color.LightGray.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Text(stringResource(R.string.publish_no_imagen))
            }
        }

        Button(
            onClick = { launcher.launch("image/*") },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF0A5734),
                contentColor = Color.White
            )
        ) {
            Text(stringResource(R.string.publish_elegir_imagen))
        }
    }
}

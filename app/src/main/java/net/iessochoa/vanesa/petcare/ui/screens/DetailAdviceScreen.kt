package net.iessochoa.vanesa.petcare.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import net.iessochoa.vanesa.petcare.data.model.wrapper.mapper.advice.toDetailUI
import net.iessochoa.vanesa.petcare.ui.components.BackButton
import net.iessochoa.vanesa.petcare.ui.components.DetailItem
import net.iessochoa.vanesa.petcare.ui.components.LoadingScreen
import net.iessochoa.vanesa.petcare.ui.viewModel.advice.AdviceDetailViewModel
import net.iessochoa.vanesa.petcare.R


/**
 * Pantalla de detalle de un Advice
 * El ViewModel se encargará de cargarlo desde la API.
 */
@Composable
fun DetailAdviceScreen(
    adviceId: Long,
    onBack: () -> Unit,
    viewModel: AdviceDetailViewModel = viewModel()
) {
    //Estado del advice cargado desde la API
    val advice = viewModel.uiState.collectAsState().value

    val isLoading = viewModel.isLoading.collectAsState().value

    //Cargar el advice cuando se entra en la pantalla
    LaunchedEffect(adviceId) {
        viewModel.loadAdvice(adviceId)
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {

        BackButton(onClick = onBack)

        Spacer(modifier = Modifier.height(8.dp))

        //Si ya está cargado, mostrarlo
        when {
            isLoading -> LoadingScreen()   //SPINNER

            advice != null -> DetailItem(advice.toDetailUI())

            else -> Text(stringResource(R.string.error_cargar_consejo)) //Sin internet o error
        }
    }
}


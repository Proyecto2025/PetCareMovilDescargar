package net.iessochoa.vanesa.petcare.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import net.iessochoa.vanesa.petcare.data.model.dto.advice.AllAdvicesDto
import net.iessochoa.vanesa.petcare.data.model.wrapper.AdviceVisualData
import net.iessochoa.vanesa.petcare.ui.components.DropDown
import net.iessochoa.vanesa.petcare.ui.components.ItemCard
import net.iessochoa.vanesa.petcare.ui.components.LoadingScreen
import net.iessochoa.vanesa.petcare.ui.viewModel.advice.AdviceViewModel
import net.iessochoa.vanesa.petcare.R

@Composable
fun AdviceListScreen(
    modifier: Modifier = Modifier,
    viewModel: AdviceViewModel = viewModel(),
    onAdviceSelected: (AllAdvicesDto) -> Unit
) {

    LaunchedEffect(Unit) {
        if (viewModel.uiState.value.advices.isEmpty()) {
            viewModel.getAllAdvices()
        }
    }


    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = stringResource(R.string.titulo_consejos),
            color = Color(0xFF0C653D),
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold
        )


        Spacer(modifier = Modifier.height(20.dp))

        DropDown(
            opciones = listOf(
                stringResource(R.string.categoria_todo),
                stringResource(R.string.categoria_comida),
                stringResource(R.string.categoria_higiene),
                stringResource(R.string.categoria_accesorios)
            ),
            selected = uiState.filtro,
            onSelect = { viewModel.cambiarFiltro(it) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 90.dp)
        ) {

            //Mostrar spinner mientras carga
            if (uiState.isLoading) {
                LoadingScreen()
            } else {
                //Mostrar lista o mensaje vacío
                if (uiState.listaFiltrada.isEmpty()) {
                    Text(
                        text = stringResource(R.string.mensaje_no_consejos),
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.align(Alignment.TopCenter)
                    )
                } else {
                    LazyColumn {
                        items(uiState.listaFiltrada) { advice ->
                            ItemCard(
                                item = AdviceVisualData(advice),
                                onClick = { onAdviceSelected(advice) }
                            )
                        }
                    }
                }
            }
        }
    }
}

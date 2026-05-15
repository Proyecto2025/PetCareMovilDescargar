package net.iessochoa.vanesa.petcare.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import net.iessochoa.vanesa.petcare.R

@Composable
fun TermsAndConditionsScreen(onBack: () -> Unit) {

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {

        Text(
            stringResource(R.string.terms_titulo),
            style = MaterialTheme.typography.headlineMedium,
            color = Color(0xFF0C653D)
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.terms_texto),
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF0C653D)
            )
        ) {
            Text(stringResource(R.string.terms_volver))
        }
    }
}

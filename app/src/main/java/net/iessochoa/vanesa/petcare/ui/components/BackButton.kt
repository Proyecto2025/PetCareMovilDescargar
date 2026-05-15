package net.iessochoa.vanesa.petcare.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import net.iessochoa.vanesa.petcare.R

@Composable
fun BackButton(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = stringResource(R.string.perfil_volver),
            color = Color(0xFF0C653D),
            style = MaterialTheme.typography.titleMedium
        )
    }
}

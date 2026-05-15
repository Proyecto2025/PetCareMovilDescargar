package net.iessochoa.vanesa.petcare.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import net.iessochoa.vanesa.petcare.R
import net.iessochoa.vanesa.petcare.ui.navegation.AdviceDestination
import net.iessochoa.vanesa.petcare.ui.navegation.PostDestination
import net.iessochoa.vanesa.petcare.ui.navegation.ProfileDestination
import net.iessochoa.vanesa.petcare.ui.navegation.PublishDestination

@Composable
fun PetCareBottomBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier) {

        NavigationBarItem(
            selected = currentRoute?.startsWith(PostDestination.route) == true,
            onClick = { onNavigate(PostDestination.route) },
            icon = { Icon(Icons.Filled.Pets, contentDescription = stringResource(R.string.bottom_post)) },
            label = { Text(stringResource(R.string.bottom_post)) }
        )

        NavigationBarItem(
            selected = currentRoute?.startsWith(AdviceDestination.route) == true,
            onClick = { onNavigate(AdviceDestination.route) },
            icon = { Icon(Icons.Filled.Lightbulb, contentDescription = stringResource(R.string.titulo_consejos)) },
            label = { Text(stringResource(R.string.titulo_consejos)) }
        )

        NavigationBarItem(
            selected = currentRoute?.startsWith(PublishDestination.route) == true,
            onClick = { onNavigate(PublishDestination.route) },
            icon = { Icon(Icons.Filled.AddCircle, contentDescription = stringResource(R.string.bottom_publish)) },
            label = { Text(stringResource(R.string.bottom_publish)) }
        )

        NavigationBarItem(
            selected = currentRoute?.startsWith(ProfileDestination.route) == true,
            onClick = { onNavigate(ProfileDestination.route) },
            icon = { Icon(Icons.Filled.Person, contentDescription = stringResource(R.string.perfil_titulo_pantalla)) },
            label = { Text(stringResource(R.string.perfil_titulo_pantalla)) }
        )
    }
}

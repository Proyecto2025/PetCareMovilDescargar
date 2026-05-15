package net.iessochoa.vanesa.petcare.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import net.iessochoa.vanesa.petcare.ui.components.BackButton
import net.iessochoa.vanesa.petcare.ui.components.DetailItem
import net.iessochoa.vanesa.petcare.ui.viewModel.post.DetailPostViewModel
import net.iessochoa.vanesa.petcare.ui.viewModel.post.DetailPostViewModelFactory
import net.iessochoa.vanesa.petcare.ui.advice.toDetailUI
import net.iessochoa.vanesa.petcare.ui.components.LoadingScreen
import net.iessochoa.vanesa.petcare.R

@Composable
fun DetailPostScreen(
    postId: Long,
    onBack: () -> Unit
) {
    val viewModel: DetailPostViewModel = viewModel(
        factory = DetailPostViewModelFactory(postId)
    )

    val uiState by viewModel.uiState.collectAsState()
    val userEmail by viewModel.userEmail.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        BackButton(onClick = onBack)

        Spacer(modifier = Modifier.height(8.dp))

        when {
            uiState.isLoading -> {
                LoadingScreen()
            }

            uiState.post != null -> {
                val post = uiState.post!!

                DetailItem(post.toDetailUI())

                Spacer(modifier = Modifier.height(24.dp))

                //BOTÓN CONTACTAR
                if (userEmail != null) {
                    Button(
                        onClick = {
                            val intent = Intent(Intent.ACTION_SENDTO).apply {
                                data = Uri.parse(
                                    "mailto:$userEmail" +
                                            "?subject=Consulta sobre tu publicación" +
                                            "&body=Hola, he visto tu anuncio y estoy interesad@."
                                )
                            }
                            context.startActivity(intent)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF0C653D),
                            contentColor = Color.White
                        )
                    ) {
                        Text(stringResource(R.string.contactar))
                    }
                }
            }
            else -> Text(stringResource(R.string.error_cargar_post))
        }
    }
}



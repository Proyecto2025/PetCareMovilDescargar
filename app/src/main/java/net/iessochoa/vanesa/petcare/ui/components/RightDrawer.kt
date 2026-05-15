package net.iessochoa.vanesa.petcare.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color

@Composable
fun RightDrawer( //Para q al darle click al menu se abra de der a izq
    isOpen: Boolean,
    onClose: () -> Unit,
    drawerContent: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    //Calcular el tamaño de la pantalla
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    Box(Modifier.fillMaxSize()) {

        //Contenido principal
        content()

        //Fondo oscuro detrás del menú (como IG)
        AnimatedVisibility(
            visible = isOpen,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f)) //Fondo semitransparente
                    .clickable { onClose() } //Cerrar al tocar fuera
            )
        }

        //Drawer animado desde la derecha
        AnimatedVisibility(
            visible = isOpen,
            enter = slideInHorizontally(initialOffsetX = { it }), //Entra desde la derecha
            exit = slideOutHorizontally(targetOffsetX = { it })   //Sale hacia la derecha
        ) {

            //Ocupa toda la pantalla y alinea el drawer a la derecha
            Box(
                modifier = Modifier
                    .fillMaxSize(), //Ocupa toda la pantalla
                contentAlignment = Alignment.CenterEnd
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxHeight() //Todo el alto de la pantalla
                        .width(screenWidth * 0.80f) //60% de pantalla
                ) {
                    Surface(
                        modifier = Modifier.fillMaxHeight()  //Ocupa todo el alto
                            .width(screenWidth * 0.80f), //Hay q ponerlo aqui tmb pq o si no, no lo hace
                        color = Color.White,
                        shadowElevation = 8.dp //Sombra para efecto de panel
                    ) {


                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(20.dp),
                            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(20.dp) //Separación entre elementos (No se pone abajo pq no afecta a cada elemento)
                        ) {
                            drawerContent()
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRightDrawer() {
    RightDrawer(
        isOpen = true, //Ver el preview
        onClose = {},
        drawerContent = {
            Text("Seguridad")
            Text("Cambiar datos personales")
            Text("Cerrar sesión", color = Color.Red)
            Text("Eliminar cuenta", color = Color.Red)
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Text("Contenido principal")
        }
    }
}

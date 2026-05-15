package net.iessochoa.vanesa.petcare.ui.components

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

//Para q no salga la barra de navegación por defecto de los móviles
@Composable
fun EnableImmersiveMode(isKeyboardOpen: Boolean) {
    val activity = LocalActivity.current ?: return

    SideEffect {
        WindowCompat.setDecorFitsSystemWindows(activity.window, false)

        val controller = WindowInsetsControllerCompat(
            activity.window,
            activity.window.decorView
        )

        if (isKeyboardOpen) {
            //Mostrar barras cuando el teclado está abierto
            controller.show(WindowInsetsCompat.Type.systemBars())
        } else {
            //Ocultar barras cuando el teclado está cerrado
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
}


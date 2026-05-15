package net.iessochoa.vanesa.petcare.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import net.iessochoa.vanesa.petcare.data.local.provider.UserSessionRepositoryProvider
import net.iessochoa.vanesa.petcare.ui.components.EnableImmersiveMode
import net.iessochoa.vanesa.petcare.ui.components.PetCareBottomBar
import net.iessochoa.vanesa.petcare.ui.components.rememberKeyboardState
import net.iessochoa.vanesa.petcare.ui.navegation.PetCareNavHost
import net.iessochoa.vanesa.petcare.ui.viewModel.user.UserViewModel
import net.iessochoa.vanesa.petcare.ui.viewModel.user.UserViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetCareApp(
    navController: NavHostController = rememberNavController()
) {
    val isKeyboardOpen by rememberKeyboardState()

    EnableImmersiveMode(isKeyboardOpen)

    val context = LocalContext.current

    //Crear repositorio de sesión
    val sessionRepository = UserSessionRepositoryProvider.get(context)

    //Crear ViewModel con factory
    val viewModel: UserViewModel = viewModel(
        factory = UserViewModelFactory(sessionRepository)
    )

    //Observamos la ruta actual
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    //Para q al abrir el menu lateral, se esconda el bottomBar
    var isDrawerOpen by remember { mutableStateOf(false) }

    val isLoggedInDataStore by sessionRepository.isLoggedIn.collectAsState(initial = null)
    val loginUiState by viewModel.loginUiState.collectAsState()

    val isLoggedInFinal =
        (loginUiState.loginExitoso) || (isLoggedInDataStore == true)


    //Rutas donde NO debe aparecer la bottom bar
    val hideBottomBarRoutes = listOf("login", "register", "detailPost", "detailAdvice")


    MaterialTheme(colorScheme = lightColorScheme()) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp)
        ) {

            //Contenido principal
            PetCareNavHost(
                navController = navController,
                modifier = Modifier.fillMaxSize(),
                onDrawerStateChange = { isOpen -> isDrawerOpen = isOpen }
            )

            //Bottom bar ANCLADA y escondida
            if (
                isLoggedInFinal &&          //para q pille tbn el detailPost/id
                hideBottomBarRoutes.none { currentRoute?.startsWith(it) == true } &&
                !isDrawerOpen
            ) {
                PetCareBottomBar(
                    currentRoute = currentRoute,
                    onNavigate = { route -> navController.navigate(route) },
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }
    }
}



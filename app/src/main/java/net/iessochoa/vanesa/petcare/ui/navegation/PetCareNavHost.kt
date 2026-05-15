package net.iessochoa.vanesa.petcare.ui.navegation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import net.iessochoa.vanesa.petcare.ui.components.LoadingScreen
import net.iessochoa.vanesa.petcare.ui.screens.*
import net.iessochoa.vanesa.petcare.ui.viewModel.advice.AdviceViewModel
import net.iessochoa.vanesa.petcare.ui.viewModel.user.UserViewModel


@Composable
fun PetCareNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onDrawerStateChange: (Boolean) -> Unit
) {

    val userViewModel: UserViewModel = viewModel()

    //Casos de login (DataStore + memoria)
    val isLoggedInDataStore by userViewModel.isLoggedIn.collectAsState(initial = null)
    val loginUiState by userViewModel.loginUiState.collectAsState()

    //Estado REAL del login
    val isLoggedInFinal =
        (loginUiState.loginExitoso) || (isLoggedInDataStore == true)

    //Mientras DataStore carga, aparece el circulo girando
    if (isLoggedInDataStore == null) {
        LoadingScreen()
        return
    }

    //Elegir pantalla inicial según si hay sesión guardada
    val startDestination =
        if (isLoggedInFinal) PostDestination.route
        else LoginDestination.route


    key(isLoggedInFinal) { //Se actualiza después de cerrar sesión o cambiar de cuenta

        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = modifier
        ) {

            //LOGIN
            composable(LoginDestination.route) {
                LoginScreen(
                    viewModel = userViewModel,
                    onLoginSuccess = {
                        navController.navigate(PostDestination.route) {
                            popUpTo(LoginDestination.route) { inclusive = true }
                            launchSingleTop = true
                        }
                        userViewModel.resetLoginExitoso()
                    },
                    onNavigateToRegister = { navController.navigate(RegisterDestination.route) },
                    onNavigateToTerms = { navController.navigate("terminos") }
                )
            }

            //REGISTRO
            composable(RegisterDestination.route) {
                RegisterScreen(
                    viewModel = userViewModel,
                    onRegisterSuccess = {
                        navController.navigate(PostDestination.route) {
                            popUpTo(LoginDestination.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    onNavigateToTerms = { navController.navigate("terminos") }
                )
            }

            //LISTA DE POSTS
            composable(PostDestination.route) {
                PostListScreen(
                    onPostSelected = { post ->
                        navController.navigate("${DetailPostDestination.route}/${post.id}")
                    }
                )
            }

            //LISTA DE ADVICE
            composable(AdviceDestination.route) { backStackEntry ->

                val adviceViewModel: AdviceViewModel =
                    viewModel(backStackEntry)

                AdviceListScreen(
                    viewModel = adviceViewModel,
                    onAdviceSelected = { advice ->
                        navController.navigate("${DetailAdviceDestination.route}/${advice.id}")
                    }
                )
            }


            //DETALLE ADVICE
            composable("${DetailAdviceDestination.route}/{id}") { backStackEntry ->

                val id = backStackEntry.arguments?.getString("id")!!.toLong()

                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(AdviceDestination.route)
                }

                val adviceViewModel: AdviceViewModel =
                    viewModel(parentEntry)

                DetailAdviceScreen(
                    adviceId = id,
                    onBack = { navController.popBackStack() }
                )
            }



            //DETALLE POST
            composable("${DetailPostDestination.route}/{id}") { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")!!.toLong()

                DetailPostScreen(
                    postId = id,
                    onBack = { navController.popBackStack() }
                )
            }

            //PUBLICAR
            composable(PublishDestination.route) {
                PublishScreen(
                    onBack = { navController.popBackStack() },
                    onPublishSuccess = {
                        navController.navigate(PostDestination.route) {
                            popUpTo(PublishDestination.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }

            //PERFIL
            composable(
                route = ProfileDestination.route +
                        "?passwordChanged={passwordChanged}&dataChanged={dataChanged}",
                arguments = listOf(
                    navArgument("passwordChanged") {
                        type = NavType.BoolType
                        defaultValue = false
                    },
                    navArgument("dataChanged") {
                        type = NavType.BoolType
                        defaultValue = false
                    }
                )
            ) { backStackEntry ->

                val passwordChanged =
                    backStackEntry.arguments?.getBoolean("passwordChanged") ?: false

                val dataChanged =
                    backStackEntry.arguments?.getBoolean("dataChanged") ?: false

                //Estado REAL del login dentro del perfil
                val isLoggedInDataStoreProfile by userViewModel.isLoggedIn.collectAsState(initial = null)
                val loginUiStateProfile by userViewModel.loginUiState.collectAsState()

                val isLoggedInFinalProfile =
                    loginUiStateProfile.loginExitoso || (isLoggedInDataStoreProfile == true)

                if (isLoggedInDataStoreProfile == null) {
                    LoadingScreen()
                    return@composable
                }

                //Si NO está logeado va a login
                if (!isLoggedInFinalProfile) {
                    LaunchedEffect(Unit) {
                        navController.navigate(LoginDestination.route) {
                            popUpTo(0)
                        }
                    }
                    return@composable
                }

                //Cargar perfil REAL desde la API
                LaunchedEffect(Unit) {
                    userViewModel.initPerfil()
                }

                ProfileScreen(
                    viewModel = userViewModel,
                    passwordChanged = passwordChanged,
                    dataChanged = dataChanged,
                    onPostSelected = { post ->
                        navController.navigate("${DetailPostDestination.route}/${post.id}")
                    },
                    onCambiarDatos = {
                        navController.navigate(ChangeDataDestination.route)
                    },
                    onSeguridad = {
                        navController.navigate(SecurityDestination.route)
                    },
                    onEliminarCuenta = {
                        navController.navigate(LoginDestination.route) {
                            popUpTo(0)
                        }
                    },
                    onDrawerStateChange = onDrawerStateChange
                )
            }

            //DATOS PERSONALES
            composable(ChangeDataDestination.route) {

                val userId by userViewModel.sessionUserId.collectAsState(initial = -1)

                LaunchedEffect(userId) {
                    if (userId != -1L) {
                        userViewModel.cargarPerfil(userId)
                    }
                }

                PersonalDataScreen(
                    viewModel = userViewModel,
                    onBack = { dataChanged ->
                        if (dataChanged) {
                            navController.navigate(
                                ProfileDestination.route +
                                        "?passwordChanged=false&dataChanged=true"
                            ) {
                                popUpTo(ProfileDestination.route) { inclusive = true }
                            }
                        } else {
                            navController.popBackStack()
                        }
                    }
                )
            }

            //SEGURIDAD
            composable(SecurityDestination.route) {

                val isLoggedInDataStoreSecurity by userViewModel.isLoggedIn.collectAsState(initial = null)
                val loginUiStateSecurity by userViewModel.loginUiState.collectAsState()

                val isLoggedInFinalSecurity =
                    loginUiStateSecurity.loginExitoso || (isLoggedInDataStoreSecurity == true)

                if (isLoggedInDataStoreSecurity == null) {
                    LoadingScreen()
                    return@composable
                }

                if (!isLoggedInFinalSecurity) {
                    LaunchedEffect(Unit) {
                        navController.navigate(LoginDestination.route) {
                            popUpTo(0)
                        }
                    }
                    return@composable
                }

                val userId by userViewModel.sessionUserId.collectAsState(initial = -1)

                SecurityScreen(
                    viewModel = userViewModel,
                    onBack = { passwordChanged ->
                        if (passwordChanged) {
                            navController.navigate(
                                ProfileDestination.route +
                                        "?passwordChanged=true&dataChanged=false"
                            ) {
                                popUpTo(ProfileDestination.route) { inclusive = true }
                            }
                        } else {
                            navController.popBackStack()
                        }
                    }
                )
            }

            //TÉRMINOS
            composable("terminos") {
                TermsAndConditionsScreen(
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}

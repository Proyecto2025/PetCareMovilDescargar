package net.iessochoa.vanesa.petcare.ui.post

import net.iessochoa.vanesa.petcare.model.Post

data class PostUiState(

    //Lista completa de posts
    val posts: List<Post> = emptyList(),

    //Lista filtrada según los filtros aplicados
    val listaFiltrada: List<Post> = emptyList(),

    //Filtro de categoría
    val filtro: String = "TODO",

    //Filtro de animal
    val filtroAnimal: String = "TODO",

    //Ubicación seleccionada
    val ciudad: String = "",

    //Lo que escribe el usuario
    val textoUbicacion: String = "",

    //Sugerencias de Geoapify
    val sugerenciasUbicacion: List<String> = emptyList(),

    //Para mostrar el círculo de carga
    val isLoading: Boolean = false
)
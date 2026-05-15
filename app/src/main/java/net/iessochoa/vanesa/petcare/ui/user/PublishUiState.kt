package net.iessochoa.vanesa.petcare.ui.user

import android.net.Uri

data class PublishUiState(
    val tipoPublicar: String = "POST", //Post por defecto (Post/Consejo)
    val paso: Int = 1, //Si le ha dado a siguiente

    // POST
    val tipoPublicacion: String = "ADOPCION",
    val tipoAnimal: String = "PERRO",
    val titulo: String = "",

    //provincia + municipio
    val provincia: String = "",
    val municipio: String = "",

    val descripcionCorta: String = "",
    val descripcionLarga: String = "",
    val detallesExtra: String = "",
    val donarPertenencias: Boolean? = null,
    val pertenencias: String = "",

    //CONSEJO
    val tipoConsejo: String = "COMIDA",
    val subtitulo: String = "",

    //Imagen (Uri=dirección de la imagen en el móvil)
    val imagen: Uri? = null,

    //Autocompletar
    val sugerenciasProvincia: List<String> = emptyList(),
    val sugerenciasMunicipio: List<String> = emptyList(),

    //Si se ha llegado a publicar
    val publicado: Boolean = false,

    //Lo q se mustra al elegir
    val provinciaVisible: String = "",
    val municipioVisible: String = "",

    // Lo q escribe el usuario en el TextField
    val textoProvincia: String = "",
    val textoMunicipio: String = "",

    val error: String? = null,

    val isLoading: Boolean = false

)


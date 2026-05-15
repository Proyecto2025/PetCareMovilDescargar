package net.iessochoa.vanesa.petcare.ui.advice

import net.iessochoa.vanesa.petcare.model.Advice
import net.iessochoa.vanesa.petcare.model.Post

//Se crea para usarlo en el detailItem y en los screens de detail
data class DetailUI(
    val titulo: String,
    val subtitulo: String,
    val ubicacion: String?, // null en Advice
    val imagen: String,
    val descripcionLarga: String,
    val datosExtra: String,
    val pertenencias: List<String>? // null en Advice
)

fun Advice.toDetailUI() = DetailUI(
    titulo = titulo,
    subtitulo= subtitulo,
    ubicacion = null,
    imagen = imagen,
    descripcionLarga = descripcionLarga,
    datosExtra = datosExtra,
    pertenencias = null
)

fun Post.toDetailUI() = DetailUI(
    titulo = titulo,
    subtitulo= subtitulo,
    ubicacion = ubicacion,
    imagen = imagen,
    descripcionLarga = descripcionLarga,
    datosExtra = datosExtra,
    pertenencias = pertenencias
)

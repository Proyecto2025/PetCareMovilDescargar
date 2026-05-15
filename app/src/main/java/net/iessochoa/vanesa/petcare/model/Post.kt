package net.iessochoa.vanesa.petcare.model

import net.iessochoa.vanesa.petcare.model.common.ItemVisualData

enum class TipoAnimal {
    PERRO,
    GATO
}

enum class CategoriaPost {
    ADOPCION,
    EXTRAVIO,
    AYUDA,
    CONSEJO

}

data class Post(
    val id: Int,
    val userId: Long? = null,
    val tipoAnimal: TipoAnimal,
    val categoria: CategoriaPost,
    val titulo: String,
    val subtitulo: String,
    val nombreUsuario: String,
    val ubicacion: String, //Provincia
    val municipio: String,
    val descripcionCorta: String,
    val descripcionLarga: String,
    val datosExtra: String,
    val pertenencias: List<String>?,
    val imagen: String
) : ItemVisualData {

    override val uiCategoriaTexto: String get() = categoria.name
    override val uiTitulo: String get() = titulo
    override val uiSubtitulo: String get() = subtitulo
    override val uiNombreUsuario: String get() = nombreUsuario
    override val uiDescripcionCorta: String get() = descripcionCorta
    override val uiImagen: String get() = imagen

    //Para que se mustre ambas cosas en el post
    override val uiUbicacion: String? get() = "$ubicacion/$municipio"
    override val uiMunicipio: String? get() = municipio

}

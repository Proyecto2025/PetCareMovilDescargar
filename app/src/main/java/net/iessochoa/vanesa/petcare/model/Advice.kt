package net.iessochoa.vanesa.petcare.model

import net.iessochoa.vanesa.petcare.model.common.ItemVisualData

enum class Categoria {
    COMIDA,
    HIGIENE,
    ACCESORIOS
}

data class Advice(
    val id: Int,
    val categoria: Categoria,
    val titulo: String,
    val subtitulo: String,
    val nombreUsuario: String,
    val descripcionCorta: String,
    val descripcionLarga: String,
    val datosExtra: String,
    val imagen: String
) : ItemVisualData {

    override val uiCategoriaTexto: String get() = categoria.name
    override val uiTitulo: String get() = titulo
    override val uiSubtitulo: String get() = subtitulo
    override val uiNombreUsuario: String get() = nombreUsuario
    override val uiDescripcionCorta: String get() = descripcionCorta
    override val uiImagen: String get() = imagen
    override val uiUbicacion: String? get() = null
    override val uiMunicipio: String? get() = null

}

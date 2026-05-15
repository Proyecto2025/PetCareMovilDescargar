package net.iessochoa.vanesa.petcare.model.common

//Se pone ui porque si no da error en post (Se lia con los nombres)
//Se usa en advice, post y ItemCard (Para reutilizar)
interface ItemVisualData {
    val uiNombreUsuario: String
    val uiCategoriaTexto: String
    val uiImagen: String
    val uiTitulo: String

    val uiSubtitulo: String
    val uiDescripcionCorta: String

    val uiUbicacion: String?
    val uiMunicipio: String?

}

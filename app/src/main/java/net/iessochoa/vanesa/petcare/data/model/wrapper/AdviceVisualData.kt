package net.iessochoa.vanesa.petcare.data.model.wrapper

import net.iessochoa.vanesa.petcare.data.model.dto.advice.AllAdvicesDto
import net.iessochoa.vanesa.petcare.model.common.ItemVisualData

/**
 * Adaptador para mostrar Advice en ItemCard
 * Convierte AllAdvicesDto en ItemVisualData
 */
data class AdviceVisualData(
    private val dto: AllAdvicesDto
) : ItemVisualData {

    override val uiNombreUsuario: String
        get() = dto.userName

    override val uiCategoriaTexto: String
        get() = dto.category.name   //AdviceCategory enum -> texto

    override val uiImagen: String
        get() = dto.image

    override val uiTitulo: String
        get() = dto.title

    override val uiSubtitulo: String
        get() = dto.subtitle

    override val uiDescripcionCorta: String
        get() = dto.shortDescription

    //Advice NO tiene ubicación
    override val uiUbicacion: String?
        get() = null

    //Advice NO tiene municipio
    override val uiMunicipio: String?
        get() = null
}

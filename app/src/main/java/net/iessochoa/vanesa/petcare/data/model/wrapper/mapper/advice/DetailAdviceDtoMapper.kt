package net.iessochoa.vanesa.petcare.data.model.wrapper.mapper.advice

import net.iessochoa.vanesa.petcare.data.model.dto.advice.DetailAdviceDto
import net.iessochoa.vanesa.petcare.ui.advice.DetailUI

fun DetailAdviceDto.toDetailUI(): DetailUI {
    return DetailUI(
        titulo = this.title,
        subtitulo = this.subtitle,
        ubicacion = null,                 //Advice NO tiene ubicación
        imagen = this.image,
        descripcionLarga = this.longDescription,
        datosExtra = this.extraDetail ?: "",
        pertenencias = null               //Advice NO tiene pertenencias
    )
}
package net.iessochoa.vanesa.petcare.data.model.wrapper.mapper.advice

import net.iessochoa.vanesa.petcare.data.model.dto.advice.AllAdvicesDto
import net.iessochoa.vanesa.petcare.ui.advice.DetailUI

fun AllAdvicesDto.toDetailUI(): DetailUI {
    return DetailUI(
        titulo = this.title,
        subtitulo = this.subtitle,
        ubicacion = null,                 //Advice NO tiene ubicación
        imagen = this.image,
        descripcionLarga = this.shortDescription,
        datosExtra = "",                  //Advice NO tiene datos extra
        pertenencias = null               //Advice NO tiene pertenencias
    )
}

package net.iessochoa.vanesa.petcare.data.model.wrapper.mapper.post

import net.iessochoa.vanesa.petcare.data.model.dto.post.DetailPostDto
import net.iessochoa.vanesa.petcare.model.CategoriaPost
import net.iessochoa.vanesa.petcare.model.Post
import net.iessochoa.vanesa.petcare.model.TipoAnimal

fun DetailPostDto.toDomain(): Post {
    return Post(
        id = id.toInt(),
        userId = userId,
        tipoAnimal = TipoAnimal.PERRO,        //API NO lo manda
        categoria = CategoriaPost.ADOPCION,   //API NO lo manda
        titulo = title,
        subtitulo = subtitle,
        nombreUsuario = "",
        ubicacion = location,
        municipio = municipality,
        descripcionCorta = "",
        descripcionLarga = longDescription,
        datosExtra = extraDetails ?: "",
        pertenencias = belongings,
        imagen = image
    )
}

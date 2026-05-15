package net.iessochoa.vanesa.petcare.data.model.wrapper.mapper.post

import net.iessochoa.vanesa.petcare.data.model.dto.post.AllPostsDto
import net.iessochoa.vanesa.petcare.model.CategoriaPost
import net.iessochoa.vanesa.petcare.model.Post
import net.iessochoa.vanesa.petcare.model.TipoAnimal

fun AllPostsDto.toDomain(): Post {
    return Post(
        id = id.toInt(),
        userId = null,
        tipoAnimal = TipoAnimal.valueOf(typeAnimal.name),
        categoria = CategoriaPost.valueOf(categoryPost.name),
        titulo = title,
        subtitulo = subtitle,
        nombreUsuario = userName,
        ubicacion = location,
        municipio = municipality,
        descripcionCorta = shortDescription,
        descripcionLarga = "",      //No viene en la API de lista
        datosExtra = "",            //No viene en la API de lista
        pertenencias = null,        //No viene en la API de lista
        imagen = image
    )
}




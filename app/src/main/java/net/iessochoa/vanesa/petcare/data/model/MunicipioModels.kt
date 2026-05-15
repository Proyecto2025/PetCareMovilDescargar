package net.iessochoa.vanesa.petcare.data.model

import kotlinx.serialization.Serializable

@Serializable
data class MunicipioItem(
    val DMUN50: String
)

@Serializable
data class MunicipioResponse(
    val data: List<MunicipioItem>
)
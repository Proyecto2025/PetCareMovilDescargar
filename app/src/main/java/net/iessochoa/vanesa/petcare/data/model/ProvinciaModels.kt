package net.iessochoa.vanesa.petcare.data.model

import kotlinx.serialization.Serializable

//Se usa en filtrar por ubi
@Serializable
data class ProvinciaItem(
    val PRO: String,
    val CPRO: String
)


@Serializable
data class ProvinciaResponse(
    val data: List<ProvinciaItem>
)
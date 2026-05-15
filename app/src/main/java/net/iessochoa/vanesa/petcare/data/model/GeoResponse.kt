package net.iessochoa.vanesa.petcare.data.model

import kotlinx.serialization.Serializable

@Serializable
data class GeoResponse(
    val features: List<Feature>
)
@Serializable
data class Feature(
    val properties: Properties
)
@Serializable
data class Properties(
    val city: String?, //Elche
    val county: String?, //Alicante
    //val country: String?, //España
)




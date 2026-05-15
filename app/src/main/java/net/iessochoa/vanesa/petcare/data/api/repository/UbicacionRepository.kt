package net.iessochoa.vanesa.petcare.data.api.repository


import net.iessochoa.vanesa.petcare.data.api.UbicacionApi

object UbicacionRepository {

    //Sugiere provincias y ciudades
    suspend fun sugerenciasGeoapify(texto: String, apiKey: String): List<String> {
        return try {
            val response = UbicacionApi.service.buscarUbicacion(
                texto = texto,
                apiKey = apiKey
            )

            response.features
                .flatMap { listOfNotNull(it.properties.city, it.properties.county) }
                .distinct()
                .take(10)

        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun buscarProvincias(texto: String): List<String> {
        return try {
            val response = UbicacionApi.service.buscarUbicacion(
                texto = texto,
                apiKey = "ff9264ab935a4fb38d66de855f47f972"
            )

            response.features
                .mapNotNull { it.properties.county }
                .distinct()
                .take(10)

        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun buscarMunicipios(provincia: String, municipio: String): List<String> {
        return try {
            val response = UbicacionApi.service.buscarUbicacion(
                texto = municipio,
                apiKey = "ff9264ab935a4fb38d66de855f47f972"
            )

            response.features
                .filter { it.properties.county == provincia }
                .mapNotNull { it.properties.city ?: it.properties.city }
                .distinct()
                .take(10)

        } catch (e: Exception) {
            emptyList()
        }
    }

}
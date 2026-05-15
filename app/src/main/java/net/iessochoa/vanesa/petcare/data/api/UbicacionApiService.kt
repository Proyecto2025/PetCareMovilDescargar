package net.iessochoa.vanesa.petcare.data.api

import net.iessochoa.vanesa.petcare.data.model.GeoResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface UbicacionApiService {

    @GET("geocode/search")
    suspend fun buscarUbicacion(
        @Query("text") texto: String,
        @Query("filter") filtro: String = "countrycode:es",
        @Query("apiKey") apiKey: String
    ): GeoResponse
}


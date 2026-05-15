package net.iessochoa.vanesa.petcare.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object UbicacionApi {

    //https://api.geoapify.com/v1/geocode/search?text=Elche&filter=countrycode:es&apiKey=ff9264ab935a4fb38d66de855f47f972
    private const val BASE_URL = "https://api.geoapify.com/v1/"

    val service: UbicacionApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UbicacionApiService::class.java)
    }
}
package uk.ac.tees.mad.e4552051.pawpal.data.places

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object PlacesApi {
    val service: PlacesApiService = Retrofit.Builder()
        .baseUrl("https://maps.googleapis.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(PlacesApiService::class.java)
}
package uk.ac.tees.mad.e4552051.pawpal.data.places

import retrofit2.http.GET
import retrofit2.http.Query

interface PlacesApiService {

    @GET("maps/api/place/nearbysearch/json")
    suspend fun getNearbyVets(
        @Query("location") location: String,
        @Query("radius") radius: Int = 5000,
        @Query("type") type: String = "veterinary_care",
        @Query("key") apiKey: String
    ): PlacesResponse
}
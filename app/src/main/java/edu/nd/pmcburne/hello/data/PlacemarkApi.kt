package edu.nd.pmcburne.hello.data

import retrofit2.http.GET

interface PlacemarkApi {
    @GET("placemarks.json")
    suspend fun getPlacemarks(): List<PlacemarkResponse>
}
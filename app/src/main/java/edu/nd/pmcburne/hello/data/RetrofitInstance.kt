package edu.nd.pmcburne.hello.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val api: PlacemarkApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.cs.virginia.edu/~wxt4gm/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PlacemarkApi::class.java)
    }
}
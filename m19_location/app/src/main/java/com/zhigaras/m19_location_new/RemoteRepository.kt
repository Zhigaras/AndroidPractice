package com.zhigaras.m19_location_new

import com.zhigaras.m19_location_new.model.ApiResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

private const val API_KEY = "a873a4b256d94b6a814d369585c4ec3b"
private const val BASE_URL = "https://api.geoapify.com/v2/"

interface PlacesInterface {
    
    @Headers("X-API-KEY: $API_KEY")
    @GET("places?categories=tourism.sights")
    suspend fun findPlaces(
        @Query("bias") lonLat: String //bias=proximity:51.367013,51.233385
    ): ApiResponse
}

class RemoteRepository {
    
    private val retrofit = Retrofit
        .Builder()
        .client(
            OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().also {
                it.level = HttpLoggingInterceptor.Level.BODY
            }).build()
        )
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    
    val placesApi: PlacesInterface = retrofit.create(PlacesInterface::class.java)
    
}
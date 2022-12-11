package edu.skillbox.m16_architecture.data

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import javax.inject.Inject


private const val BASE_URL = "https://www.boredapi.com/api/"

class RemoteRepository @Inject constructor() {

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val searchUsefulActivityApi: SearchUsefulActivityApi =
        retrofit.create(SearchUsefulActivityApi::class.java)
}

interface SearchUsefulActivityApi {

    @GET("activity/")
    suspend fun findUsefulActivity(): Response<UsefulActivityDto>
}

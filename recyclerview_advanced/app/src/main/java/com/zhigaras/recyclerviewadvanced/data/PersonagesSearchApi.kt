package com.zhigaras.recyclerviewadvanced.data

import com.zhigaras.recyclerviewadvanced.model.ApiReply
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://rickandmortyapi.com/api/"

interface PersonagesSearchApi {
    
    @GET("character")
    suspend fun findPersonages(
        @Query("page") page: Int
    ): Response<ApiReply>
}

object PersonagesApi {
    private val retrofit = Retrofit
        .Builder()
        .client(
            OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().also {
                it.level = HttpLoggingInterceptor.Level.BODY
            }).build()
        )
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
    
    val personagesSearchApi: PersonagesSearchApi = retrofit.create(PersonagesSearchApi::class.java)
}
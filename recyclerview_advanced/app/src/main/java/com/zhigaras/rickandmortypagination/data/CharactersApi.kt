package com.zhigaras.rickandmortypagination.data

import com.zhigaras.rickandmortypagination.model.ReplyModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject

private const val BASE_URL = "https://rickandmortyapi.com/api/"

interface CharactersSearchApi {
    
    @GET("character")
    suspend fun findRoverPhotos(
        @Query("page") page: Int
    ): Response<ReplyModel>
}

class CharactersApi @Inject constructor(){
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
    
    val charactersSearchApi: CharactersSearchApi = retrofit.create(CharactersSearchApi::class.java)
}
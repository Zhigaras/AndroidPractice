package edu.skillbox.m17_recyclerview.data


import edu.skillbox.m17_recyclerview.entity.MarsRoversPhotosList
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import javax.inject.Inject

private const val API_KEY = "17QbFxXVegzv03aP9CuAZBM8IbjexdliiMUwr2p6"
private const val BASE_URL = "https://api.nasa.gov/"

interface NasaMarsRoverApi {

    @Headers("X-API-KEY: $API_KEY")
    @GET("mars-photos/api/v1/rovers/curiosity/photos")
    suspend fun findRoverPhotos(
        @Query("sol") sol: Int
    ): MarsRoversPhotosList
}

class RemoteRepository @Inject constructor(){
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

    val nasaMarsRoverApi: NasaMarsRoverApi = retrofit.create(NasaMarsRoverApi::class.java)
}
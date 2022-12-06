package edu.skillbox.m14retrofit

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://randomuser.me"

object RetrofitInstance {

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val searchUserApi: SearchUserApi = retrofit.create(SearchUserApi::class.java)
}

interface SearchUserApi {

    @GET("/api")
    suspend fun getUser(@Query("inc") inc: String = listOf(
        "gender", "name", "location", "email", "dob", "picture"
    ).joinToString(separator = ",")): Response<UserModel>

}
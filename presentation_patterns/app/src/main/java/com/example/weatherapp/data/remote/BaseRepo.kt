package com.example.weatherapp.data.remote

import com.example.weatherapp.data.locale.db.WeatherItem
import com.example.weatherapp.data.remote.responses.WeatherResponse
import com.example.weatherapp.domain.ApiResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

abstract class BaseRepo {
    suspend fun safeApiCall(apiToBeCalled: suspend () -> Response<WeatherResponse>): ApiResult<WeatherItem> {
        return withContext(Dispatchers.IO) {
            try {
                val response: Response<WeatherResponse> = apiToBeCalled()
                
                if (response.isSuccessful) {
                    ApiResult.Success(_data = response.body()?.toWeatherItem())
                } else {
                    ApiResult.Error(
                        exception = if (response.code() == 400) {
                            "No location found matching. Go back and try again."
                        } else {
                            "Something went wrong"
                        }
                    )
                }
            } catch (e: HttpException) {
                ApiResult.Error(exception = e.message ?: "Something went wrong")
            } catch (e: IOException) {
                ApiResult.Error("Please check your network connection")
            } catch (e: Exception) {
                ApiResult.Error(exception = "Something went wrong")
            }
        }
    }
}
package com.uai.weatherappuai.data.repository

import android.util.Log
import com.uai.weatherappuai.data.model.WeatherResponse
import com.uai.weatherappuai.data.remote.ApiService
import com.uai.weatherappuai.data.remote.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class WeatherRepositoryImpl(
    private val apiService: ApiService
) : WeatherRepository {

    companion object {
        private const val TAG = "WeatherRepository"
    }

    override suspend fun getWeatherForecast(
        city: String,
        days: Int,
        aqi: String,
        alerts: String
    ): WeatherResponse? {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getForecast(
                    apiKey = RetrofitClient.API_KEY,
                    city = city,
                    days = days,
                    aqi = aqi,
                    alerts = alerts
                )
                if (response.isSuccessful) {
                    response.body()
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown API error"
                    Log.e(TAG, "API Error: ${response.code()} - $errorBody")
                    null
                }
            } catch (e: HttpException) {
                Log.e(TAG, "IOException: Network error or no internet connection.", e)
                null
            } catch (e: Exception) {
                Log.e(TAG, "Unexpected Exception: ${e.localizedMessage}", e)
                null
            }
        }
    }
}
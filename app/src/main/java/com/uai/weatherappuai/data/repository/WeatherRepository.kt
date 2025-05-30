package com.uai.weatherappuai.data.repository

import com.uai.weatherappuai.data.model.WeatherResponse

interface WeatherRepository {
    suspend fun getWeatherForecast(
        city: String,
        days: Int = 7,
        aqi: String = "yes",
        alerts: String = "yes"
    ) : WeatherResponse?
}

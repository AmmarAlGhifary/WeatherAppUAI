package com.uai.weatherappuai.data.model


import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("location")
    val location: Location,

    @SerializedName("current")
    val current: CurrentWeather,

    @SerializedName("forecast")
    val forecast: ForecastData? // Nullable if not always present or requested
)

// Nested Data Classes
data class Location(
    @SerializedName("name")
    val name: String,

    @SerializedName("region")
    val region: String,

    @SerializedName("country")
    val country: String,

    @SerializedName("lat")
    val lat: Double,

    @SerializedName("lon")
    val lon: Double,

    @SerializedName("tz_id")
    val timezoneId: String?,

    @SerializedName("localtime_epoch")
    val localtimeEpoch: Long,

    @SerializedName("localtime")
    val localtime: String
)

data class CurrentWeather(
    @SerializedName("last_updated_epoch")
    val lastUpdatedEpoch: Long,

    @SerializedName("last_updated")
    val lastUpdated: String,

    @SerializedName("temp_c")
    val tempC: Double,

    @SerializedName("temp_f")
    val tempF: Double,

    @SerializedName("is_day")
    val isDay: Int, // 1 = Yes, 0 = No

    @SerializedName("condition")
    val condition: Condition,

    @SerializedName("wind_mph")
    val windMph: Double,

    @SerializedName("wind_kph")
    val windKph: Double,

    @SerializedName("wind_degree")
    val windDegree: Int,

    @SerializedName("wind_dir")
    val windDir: String,

    @SerializedName("pressure_mb")
    val pressureMb: Double,

    @SerializedName("pressure_in")
    val pressureIn: Double,

    @SerializedName("precip_mm")
    val precipMm: Double,

    @SerializedName("precip_in")
    val precipIn: Double,

    @SerializedName("humidity")
    val humidity: Int,

    @SerializedName("cloud")
    val cloud: Int, // Cloud cover as percentage

    @SerializedName("feelslike_c")
    val feelslikeC: Double,

    @SerializedName("feelslike_f")
    val feelslikeF: Double,

    @SerializedName("vis_km")
    val visKm: Double,

    @SerializedName("vis_miles")
    val visMiles: Double,

    @SerializedName("uv")
    val uv: Double,

    @SerializedName("gust_mph")
    val gustMph: Double?,

    @SerializedName("gust_kph")
    val gustKph: Double?,

    @SerializedName("air_quality")
    val airQuality: AirQuality? // If requested
)

data class Condition(
    @SerializedName("text")
    val text: String,

    @SerializedName("icon")
    val icon: String, // URL path, e.g., //cdn.weatherapi.com/weather/64x64/day/113.png

    @SerializedName("code")
    val code: Int
)

data class ForecastData(
    @SerializedName("forecastday")
    val forecastDay: List<ForecastDay>
)

data class ForecastDay(
    @SerializedName("date")
    val date: String, // e.g., "2025-05-30"

    @SerializedName("date_epoch")
    val dateEpoch: Long,

    @SerializedName("day")
    val dayDetails: DayDetails,

    @SerializedName("astro")
    val astronomy: Astronomy?, // If astronomy data is part of the forecast

    @SerializedName("hour")
    val hour: List<HourForecast>
)

data class DayDetails(
    @SerializedName("maxtemp_c")
    val maxTempC: Double,

    @SerializedName("maxtemp_f")
    val maxTempF: Double,

    @SerializedName("mintemp_c")
    val minTempC: Double,

    @SerializedName("mintemp_f")
    val minTempF: Double,

    @SerializedName("avgtemp_c")
    val avgTempC: Double,

    @SerializedName("avgtemp_f")
    val avgTempF: Double,

    @SerializedName("maxwind_mph")
    val maxWindMph: Double,

    @SerializedName("maxwind_kph")
    val maxWindKph: Double,

    @SerializedName("totalprecip_mm")
    val totalPrecipMm: Double,

    @SerializedName("totalprecip_in")
    val totalPrecipIn: Double,

    @SerializedName("totalsnow_cm")
    val totalSnowCm: Double,

    @SerializedName("avgvis_km")
    val avgVisKm: Double,

    @SerializedName("avgvis_miles")
    val avgVisMiles: Double,

    @SerializedName("avghumidity")
    val avgHumidity: Double,

    @SerializedName("daily_will_it_rain")
    val dailyWillItRain: Int, // 1 = Yes, 0 = No

    @SerializedName("daily_chance_of_rain")
    val dailyChanceOfRain: Int, // Percentage

    @SerializedName("daily_will_it_snow")
    val dailyWillItSnow: Int, // 1 = Yes, 0 = No

    @SerializedName("daily_chance_of_snow")
    val dailyChanceOfSnow: Int, // Percentage

    @SerializedName("condition")
    val condition: Condition,

    @SerializedName("uv")
    val uv: Double
)

data class HourForecast(
    @SerializedName("time_epoch")
    val timeEpoch: Long,

    @SerializedName("time")
    val time: String, // e.g., "2025-05-30 00:00"

    @SerializedName("temp_c")
    val tempC: Double,

    @SerializedName("temp_f")
    val tempF: Double,

    @SerializedName("is_day")
    val isDay: Int,

    @SerializedName("condition")
    val condition: Condition,

    @SerializedName("wind_mph")
    val windMph: Double,

    @SerializedName("wind_kph")
    val windKph: Double,

    @SerializedName("precip_mm")
    val precipMm: Double,

    @SerializedName("humidity")
    val humidity: Int,

    @SerializedName("cloud")
    val cloud: Int,

    @SerializedName("feelslike_c")
    val feelslikeC: Double,

    @SerializedName("chance_of_rain")
    val chanceOfRain: Int,

    @SerializedName("chance_of_snow")
    val chanceOfSnow: Int,

    @SerializedName("uv")
    val uv: Double
)

data class AirQuality(
    @SerializedName("co")
    val co: Double?,

    @SerializedName("no2")
    val no2: Double?,

    @SerializedName("o3")
    val o3: Double?,

    @SerializedName("so2")
    val so2: Double?,

    @SerializedName("pm2_5")
    val pm25: Double?,

    @SerializedName("pm10")
    val pm10: Double?,

    @SerializedName("us-epa-index")
    val usEpaIndex: Int?,

    @SerializedName("gb-defra-index")
    val gbDefraIndex: Int?
)

data class Astronomy(
    @SerializedName("sunrise")
    val sunrise: String,

    @SerializedName("sunset")
    val sunset: String,

    @SerializedName("moonrise")
    val moonrise: String,

    @SerializedName("moonset")
    val moonset: String,

    @SerializedName("moon_phase")
    val moonPhase: String,

    @SerializedName("moon_illumination")
    val moonIllumination: String // Percentage
)
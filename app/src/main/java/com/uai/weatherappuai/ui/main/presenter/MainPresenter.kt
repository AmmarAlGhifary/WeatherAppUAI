package com.uai.weatherappuai.ui.main.presenter

import com.uai.weatherappuai.data.repository.WeatherRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class MainPresenter(
    private var view: MainContract.View?,
    private val weatherRepository: WeatherRepository
) : MainContract.Presenter {

    private val presenterScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private val cities = listOf("Jakarta", "Bandung", "Denpasar", "Surabaya", "Medan")
    private var currentCityIndex = 0
    private var currentCity: String = cities[currentCityIndex]


    override fun attachView(view: MainContract.View) {
        this.view = view
        loadWeatherData(currentCity)
    }

    override fun detachView() {
        presenterScope.cancel()
        this.view = null
    }

    override fun onCitySelected(city: String) {
        currentCity = city
        currentCityIndex = cities.indexOfFirst { it.equals(city, ignoreCase = true) }
        loadWeatherData(city)
    }

    override fun loadWeatherData(city: String) {
        view?.showLoading()
        presenterScope.launch {
            val weatherData = weatherRepository.getWeatherForecast(
                city = city,
                days = 7,
                aqi = "yes",
                alerts = "yes"
            )

            if (weatherData != null) {
                currentCity = weatherData.location.name
                val apiCityIndex = cities.indexOfFirst { it.equals(currentCity, ignoreCase = true) }
                if (apiCityIndex != -1) {
                    currentCityIndex = apiCityIndex
                }
                view?.displayWeatherData(weatherData)
            } else {
                view?.displayError("Failed to fetch weather data for $city. Please check your connection or try again.")
            }
            view?.hideLoading()
        }
    }

    override fun loadNextCity() {
        if (currentCityIndex == -1 && cities.isNotEmpty()) currentCityIndex =
            0
        else if (cities.isEmpty()) return
        currentCityIndex = (currentCityIndex + 1) % cities.size
        currentCity = cities[currentCityIndex]
        loadWeatherData(currentCity)
    }

    override fun loadPreviousCity() {
        if (currentCityIndex == -1 && cities.isNotEmpty()) currentCityIndex =
            0
        else if (cities.isEmpty()) return

        currentCityIndex = (currentCityIndex - 1 + cities.size) % cities.size
        currentCity = cities[currentCityIndex]
        loadWeatherData(currentCity)
    }
}
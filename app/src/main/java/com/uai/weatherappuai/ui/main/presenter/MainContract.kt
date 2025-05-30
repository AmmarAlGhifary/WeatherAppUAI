package com.uai.weatherappuai.ui.main.presenter

import com.uai.weatherappuai.data.model.WeatherResponse

interface MainContract {
    interface View{
        fun showLoading()
        fun hideLoading()
        fun displayWeatherData(weatherData: WeatherResponse)
        fun displayError(message: String)
    }

    interface Presenter{
        fun attachView(view: View)
        fun detachView()
        fun loadWeatherData(city: String)
        fun onCitySelected(city: String)
        fun loadNextCity()
        fun loadPreviousCity()
    }
}

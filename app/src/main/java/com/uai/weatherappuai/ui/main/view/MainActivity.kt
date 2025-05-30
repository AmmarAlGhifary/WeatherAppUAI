package com.uai.weatherappuai.ui.main.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.uai.weatherappuai.R
import com.uai.weatherappuai.data.model.WeatherResponse
import com.uai.weatherappuai.data.remote.RetrofitClient
import com.uai.weatherappuai.data.repository.WeatherRepositoryImpl
import com.uai.weatherappuai.databinding.ActivityMainBinding
import com.uai.weatherappuai.ui.main.adapter.ForecastAdapter
import com.uai.weatherappuai.ui.main.presenter.MainContract
import com.uai.weatherappuai.ui.main.presenter.MainPresenter
import java.text.SimpleDateFormat
import java.util.Locale

class MainActivity : AppCompatActivity(), MainContract.View {
    private lateinit var binding: ActivityMainBinding
    private lateinit var presenter: MainContract.Presenter
    private lateinit var forecastAdapter: ForecastAdapter

    private var dataLoaded = false

    private val apiDateTimeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    private val displayTimeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())


    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        splashScreen.setKeepOnScreenCondition { !dataLoaded }

        setupToolbar()
        initPresenter()
        setupRecyclerViw()
        setupListeners()

        presenter.attachView(this)
    }

    private fun initPresenter() {
        val apiService = RetrofitClient.instance
        val weatherRepository = WeatherRepositoryImpl(apiService)
        presenter = MainPresenter(this, weatherRepository)
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
    }

    private fun setupListeners() {
        binding.btnRetry.setOnClickListener {
            val currentDisplayedCityName =
                binding.tvCityName.text.toString().split(",").firstOrNull()?.trim()
            presenter.loadWeatherData(currentDisplayedCityName ?: "Jakarta")
        }
        binding.btnChangeCity.setOnClickListener {
            presenter.loadNextCity()
        }
    }

    override fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.errorLayout.visibility = View.GONE
        binding.cardCurrentWeather.visibility = View.INVISIBLE
        binding.layoutAdditionalDetails.visibility = View.INVISIBLE
        binding.rvForecast.visibility = View.INVISIBLE
        binding.tvForecastTitle.visibility = View.INVISIBLE
    }

    override fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

    @SuppressLint("SetTextI18n")
    override fun displayWeatherData(weatherData: WeatherResponse) {
        dataLoaded = true
        binding.errorLayout.visibility = View.GONE

        binding.cardCurrentWeather.visibility = View.VISIBLE
        binding.layoutAdditionalDetails.visibility = View.VISIBLE
        binding.rvForecast.visibility = View.VISIBLE
        binding.tvForecastTitle.visibility = View.VISIBLE

        binding.apply {
            tvCityName.text = getString(R.string.city_country_format, weatherData.location.name, weatherData.location.country)

            try {
                val localTimeDate = apiDateTimeFormat.parse(weatherData.location.localtime)
                tvLocalTime.text = localTimeDate?.let { displayTimeFormat.format(it) }
                    ?: weatherData.location.localtime.split(" ").lastOrNull()
            } catch (e: Exception) {
                tvLocalTime.text = weatherData.location.localtime.split(" ").lastOrNull() ?: ""
            }

            tvTemperature.text =
                getString(R.string.temperature_celsius_format, weatherData.current.tempC)
            tvCondition.text = weatherData.current.condition.text
            tvFeelsLike.text = getString(R.string.feels_like_format, weatherData.current.feelslikeC)

            Glide.with(this@MainActivity)
                .load("https:${weatherData.current.condition.icon}")
                .placeholder(R.drawable.ic_weather_placeholder)
                .error(R.drawable.ic_error_placeholder)
                .into(ivWeatherIcon)

            tvHumidity.text = getString(R.string.percentage_format, weatherData.current.humidity)
            tvWindSpeed.text =
                getString(R.string.wind_speed_kph_format, weatherData.current.windKph)
            tvUvIndex.text = weatherData.current.uv.toString() // UV index is a simple number

            weatherData.forecast?.forecastDay?.let {
                forecastAdapter.submitList(it)
            }
        }
    }

    override fun displayError(message: String) {
        dataLoaded = true
        binding.errorLayout.visibility = View.VISIBLE
        binding.tvErrorMessage.text = message

        binding.cardCurrentWeather.visibility = View.GONE
        binding.layoutAdditionalDetails.visibility = View.GONE
        binding.rvForecast.visibility = View.GONE
        binding.tvForecastTitle.visibility = View.GONE

        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }

    private fun setupRecyclerViw() {
        forecastAdapter = ForecastAdapter()
        binding.rvForecast.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = forecastAdapter
            setHasFixedSize(true)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }
}

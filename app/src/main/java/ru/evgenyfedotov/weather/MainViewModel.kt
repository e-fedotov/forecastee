package ru.evgenyfedotov.weather

import CityWeatherResponse
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.evgenyfedotov.weather.api.WeatherApi
import ru.evgenyfedotov.weather.api.dtoForecast.WeatherForecastResponse

private const val UNITS = "metric"

class MainViewModel(private val api: WeatherApi) : ViewModel() {

    private val _currentWeatherResponse = MutableLiveData<CityWeatherResponse>()
    val currentWeatherResponse: LiveData<CityWeatherResponse> = _currentWeatherResponse

    private val _forecastWeatherResponse = MutableLiveData<WeatherForecastResponse>()
    val forecastWeatherResponse: LiveData<WeatherForecastResponse> = _forecastWeatherResponse

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun getCurrentWeather(lat: Double, lon: Double) {
        viewModelScope.launch {
            try {
                _currentWeatherResponse.value = api.getWeather(lat, lon, UNITS)
            } catch (error: Throwable) {
                _error.value = error.message ?: "Failed to load current weather"
            }
        }
    }

    fun getForecastWeather(lat: Double, lon: Double) {
        viewModelScope.launch {
            try {
                _forecastWeatherResponse.value = api.getForecast(lat, lon, UNITS)
            } catch (error: Throwable) {
                _error.value = error.message ?: "Failed to load forecast"
            }
        }
    }
}

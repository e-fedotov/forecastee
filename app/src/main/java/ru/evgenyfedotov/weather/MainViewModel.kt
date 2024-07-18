package ru.evgenyfedotov.weather

import CityWeatherResponse
import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationListener
import android.location.LocationManager
import android.location.LocationRequest
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.evgenyfedotov.weather.api.WeatherApi
import ru.evgenyfedotov.weather.api.dtoForecast.WeatherForecastResponse
import java.util.concurrent.Executor

//private const val CITY_ID = 609655L
private const val UNITS = "metric"

class MainViewModel(private val api: WeatherApi) : ViewModel() {

    var currentWeatherResponse: MutableLiveData<CityWeatherResponse> = MutableLiveData()
    var forecastWeatherResponse: MutableLiveData<WeatherForecastResponse> = MutableLiveData()

//    private val mData = MutableLiveData<CityWeatherResponse>()
//    val data: LiveData<CityWeatherResponse> = mData
//
//    private val mForecastData = MutableLiveData<WeatherForecastResponse>()
//    val forecastData: LiveData<WeatherForecastResponse> = mForecastData
//
//    val completeData = MediatorLiveData<Pair<CityWeatherResponse?, WeatherForecastResponse?>>().apply {
//        addSource(data) {
//            value = Pair(it, forecastData.value)
//        }
//        addSource(forecastData) {
//            value = Pair(data.value, it)
//        }
//    }

//
//    init {
//        getCurrentWeather()
//        getForecastWeather()
//    }

    fun getCurrentWeather(lat: Double, lon: Double) {
        viewModelScope.launch {
            try {
                val response = api.getWeather(lat, lon, UNITS)
                currentWeatherResponse.value = response
//                mData.value = api.getWeather(lat, lon, UNITS)
//                mForecastData.value = api.getForecast(lat, lon, UNITS)

//                if (completeData.value?.second != null) {
//                    forecastDataSet = ForecastData(completeData.value?.second!!.list)
//                }

//                completeData.addSource(data) { value ->  completeData.value = Pair(data, forecastData) }
//                completeData.addSource(forecastData) { value -> completeData.value = Pair(data, forecastData) }

            } catch (error: Throwable) {
                error.printStackTrace()
            }
        }
    }

    fun getForecastWeather(lat: Double, lon: Double) {
        viewModelScope.launch {
            try {
                val response = api.getForecast(lat, lon, UNITS)
                forecastWeatherResponse.value = response

            } catch (error: Throwable) {
                error.printStackTrace()
            }
        }
    }

//    fun onRefresh() {
//        getCurrentLocation()
//        getForecastWeather()
//        getCurrentWeather()
//    }



}
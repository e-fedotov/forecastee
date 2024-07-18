package ru.evgenyfedotov.weather.api

import CityWeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query
import ru.evgenyfedotov.weather.api.dtoForecast.WeatherForecastResponse

interface WeatherApi {

    @GET("weather")
    suspend fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String
    ):CityWeatherResponse

    @GET("forecast")
    suspend fun getForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String
    ):WeatherForecastResponse
}
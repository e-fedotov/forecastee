package ru.evgenyfedotov.weather.forecastData

import java.time.LocalTime

data class ForecastData(val time: LocalTime, val temperature: Double, val iconLink: String)

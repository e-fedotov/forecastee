package ru.evgenyfedotov.weather.forecastData

import ru.evgenyfedotov.weather.api.dtoForecast.ForecastList
import ru.evgenyfedotov.weather.api.dtoForecast.WeatherForecastResponse
import java.time.*


class ForecastDataSet {

    private lateinit var timestampInstant: Instant
    private lateinit var date: LocalDate
    private lateinit var time: LocalTime
    private var timeTempIconList = mutableListOf<ForecastData>()
    var dataSetMap: LinkedHashMap<LocalDate, List<ForecastData>> = LinkedHashMap()

    init {
        buildData(emptyList<ForecastList>())
    }

    fun buildData(forecastDataSet: List<ForecastList>) {
        var previousDate: LocalDate? = null
        dataSetMap.clear()
        timeTempIconList.clear()

        for (list in forecastDataSet) {

            timestampInstant = Instant.ofEpochSecond(list.dt)

            date = LocalDateTime
                .ofInstant(timestampInstant, ZoneId.of("UTC"))
                .toLocalDate()

            time = LocalDateTime
                .ofInstant(timestampInstant, ZoneId.of("UTC"))
                .toLocalTime()

            when (previousDate) {
                null -> {
                    previousDate = date
                    timeTempIconList.add(ForecastData(time, list.main.temp, list.weather[0].icon))
                    dataSetMap[date] = timeTempIconList
                }

                date -> {
                    timeTempIconList.add(ForecastData(time, list.main.temp, list.weather[0].icon))
                    dataSetMap[date] = timeTempIconList
                }

                else -> {
                    timeTempIconList =
                        mutableListOf(ForecastData(time, list.main.temp, list.weather[0].icon))
                    previousDate = date
                    dataSetMap[date] = timeTempIconList
                }

            }

        }
    }

}
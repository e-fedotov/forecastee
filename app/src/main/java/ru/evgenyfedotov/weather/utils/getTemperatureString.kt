package ru.evgenyfedotov.weather.utils

import android.view.View
import ru.evgenyfedotov.weather.R
import kotlin.math.roundToInt

fun getTemperatureString (view: View, temperature: Double): String {

    val roundedTemperature = temperature.roundToInt()

    return if (roundedTemperature <= 0) {
        view.context.getString(R.string.temperature, roundedTemperature)
    } else {
        view.context.getString(R.string.positiveTemperature, roundedTemperature)
    }

}
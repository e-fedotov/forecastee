package ru.evgenyfedotov.weather.utils

import androidx.appcompat.content.res.AppCompatResources
import ru.evgenyfedotov.weather.R

fun getWindIcon(windDirection: Int): Int {

    return when (windDirection) {
        in 337..360 -> R.drawable.ic_n
        in 0..22 -> R.drawable.ic_n
        in 23..67 -> R.drawable.ic_ne
        in 68..112 -> R.drawable.ic_e
        in 113..157 -> R.drawable.ic_se
        in 158..202 -> R.drawable.ic_s
        in 203..247 -> R.drawable.ic_sw
        in 248..292 -> R.drawable.ic_w
        in 293..336 -> R.drawable.ic_nw
        else -> R.drawable.ic_n
    }

}
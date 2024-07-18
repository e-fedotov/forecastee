package ru.evgenyfedotov.weather.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextGeometricTransform
import androidx.compose.ui.unit.sp

val Typography = androidx.compose.material.Typography()

val Typography.cityName: TextStyle
    get() = TextStyle(
        fontSize = 32.sp,
        fontWeight = FontWeight.Black
    )

val Typography.currentTemperature: TextStyle
    get() = TextStyle(
        fontSize = 48.sp,
        fontWeight = FontWeight.Black
    )

val Typography.currentParameters: TextStyle
    get() = TextStyle(
        fontSize = 18.sp,
        fontWeight = FontWeight.Medium
    )

val Typography.subtitle: TextStyle
    get() = TextStyle(
        fontSize = 14.sp,
    )

val Typography.units: TextStyle
    get() = TextStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium
    )

val Typography.forecastDay: TextStyle
    get() = TextStyle(
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold
    )

val Typography.forecastTempeature: TextStyle
    get() = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium
    )

val Typography.forecastTime: TextStyle
    get() = TextStyle(
        fontSize = 12.sp,
    )

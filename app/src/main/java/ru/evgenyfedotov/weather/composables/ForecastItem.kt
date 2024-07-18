package ru.evgenyfedotov.weather.composables

import androidx.annotation.ColorRes
import androidx.annotation.StyleRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColor
import coil.compose.AsyncImage
import com.squareup.picasso.Picasso
import ru.evgenyfedotov.weather.R
import ru.evgenyfedotov.weather.forecastData.ForecastData
import ru.evgenyfedotov.weather.ui.theme.WeatherTheme
import ru.evgenyfedotov.weather.ui.theme.cityName
import ru.evgenyfedotov.weather.ui.theme.forecastTempeature
import ru.evgenyfedotov.weather.ui.theme.forecastTime
import java.time.LocalTime
import kotlin.math.roundToInt

class fakeForecastItemsProvider : PreviewParameterProvider<ForecastData> {
    override val values: Sequence<ForecastData>
        get() = sequenceOf(
            ForecastData(
                temperature = 3.0,
                time = LocalTime.MIDNIGHT,
                iconLink = "04n"
            )
        )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
fun ForecastItem(@PreviewParameter(fakeForecastItemsProvider::class) forecastData: ForecastData) {

    val forecastTemperature =
        if (forecastData.temperature.roundToInt() <= 0) {
            stringResource(R.string.temperature, forecastData.temperature.roundToInt())
        } else {
            stringResource(R.string.positiveTemperature, forecastData.temperature.roundToInt())
        }

    WeatherTheme() {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${forecastData.time}",
                color = MaterialTheme.colors.primary,
                style = MaterialTheme.typography.forecastTime,

                )

            AsyncImage(
                model = "https://openweathermap.org/img/wn/${forecastData.iconLink}@2x.png",
                contentDescription = "",
                modifier = Modifier
                    .width(32.dp)
                    .height(32.dp)
            )

            Text(
                text = forecastTemperature,
                style = MaterialTheme.typography.forecastTempeature,
                color = MaterialTheme.colors.primary
            )
        }
    }
}

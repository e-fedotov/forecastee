package ru.evgenyfedotov.weather.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.evgenyfedotov.weather.ICON_URL
import ru.evgenyfedotov.weather.R
import ru.evgenyfedotov.weather.forecastData.ForecastData
import ru.evgenyfedotov.weather.utils.getTemperatureString

open class ForecastItemAdapter(private val forecastDataList: List<ForecastData>) :
    RecyclerView.Adapter<ForecastItemAdapter.ForecastItemViewHolder>() {

    inner class ForecastItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val forecastTemp: TextView = itemView.findViewById(R.id.forecastTemp)
        private val forecastTime: TextView = itemView.findViewById(R.id.forecastTime)
        private val forecastImage: ImageView = itemView.findViewById(R.id.forecastImage)

        fun bind(result: ForecastData) {
            forecastTemp.text = getTemperatureString(itemView, result.temperature)
            forecastTime.text = result.time.toString()
            Picasso.get()
                .load(String.format(ICON_URL, result.iconLink))
                .into(forecastImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ForecastItemViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.forecast_item, parent, false)
    )

    override fun onBindViewHolder(holder: ForecastItemViewHolder, position: Int) {
        holder.bind(forecastDataList[position])
    }

    override fun getItemCount(): Int = forecastDataList.size
}

package ru.evgenyfedotov.weather.adapters

import android.content.res.Resources
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper
import ru.evgenyfedotov.weather.forecastData.ForecastDataSet
import ru.evgenyfedotov.weather.R
import ru.evgenyfedotov.weather.api.dtoForecast.ForecastList
import ru.evgenyfedotov.weather.forecastData.ForecastData
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

open class ForecastRowAdapter :
    RecyclerView.Adapter<ForecastRowAdapter.DataViewHolder>() {

    var forecastDates: List<LocalDate> = ArrayList()
    private var forecastData = ForecastDataSet()
    private var forecastDataSet = forecastData.dataSetMap

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(date: LocalDate, weather: List<ForecastData>) {
            val dayText: TextView = itemView.findViewById(R.id.dayText)
            val childRecyclerView: RecyclerView = itemView.findViewById(R.id.recyclerViewChild)
            dayText.setTextColor(
                itemView.resources.getColor(
                    R.color.black,
                    itemView.context.theme
                )
            )
            dayText.text = date.format(DateTimeFormatter.ofPattern("EEEE, dd MMMM"))
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

            if (date.dayOfWeek.value > 5) {
                dayText.setTextColor(
                    itemView.resources.getColor(
                        R.color.red,
                        itemView.context.theme
                    )
                )
            }
            Log.d("WeatherForecast", "${date.dayOfWeek.value} ")
            Log.d("WeatherForecast", "${dayText.text} ${dayText.currentTextColor} ")


            val childForecastAdapter = ForecastItemAdapter(weather)
            childRecyclerView.layoutManager =
                LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            childRecyclerView.adapter = childForecastAdapter

            OverScrollDecoratorHelper.setUpOverScroll(
                childRecyclerView,
                OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL
            )
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DataViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.forecast_row, parent, false
        )
    )

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        Log.d(
            "WeatherForecast",
            "${forecastDates[position]} ${forecastDataSet[forecastDates[position]]!!} "
        )
        holder.bind(forecastDates[position], forecastDataSet[forecastDates[position]]!!)
    }

    override fun getItemCount(): Int {
        return forecastDates.size

    }

    fun setData(newForecast: List<ForecastList>) {
        forecastDataSet.clear()
        forecastData.buildData(newForecast)
        forecastDates = forecastDataSet.keys.toMutableList()
        notifyDataSetChanged()
    }
}
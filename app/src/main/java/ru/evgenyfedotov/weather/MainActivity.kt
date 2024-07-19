package ru.evgenyfedotov.weather

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper
import ru.evgenyfedotov.weather.api.WeatherApi
import ru.evgenyfedotov.weather.databinding.ActivityMainBinding
import ru.evgenyfedotov.weather.adapters.ForecastRowAdapter
import ru.evgenyfedotov.weather.utils.getTemperatureString
import ru.evgenyfedotov.weather.utils.getWindIcon
import java.util.*
import kotlin.math.round

const val ICON_URL = "https://openweathermap.org/img/wn/%s@2x.png"

class MainActivity : AppCompatActivity(), LocationListener {

    private lateinit var binding: ActivityMainBinding

    private val forecastRowAdapter by lazy { ForecastRowAdapter() }
    private val horizontalLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    private lateinit var locationManager: LocationManager

    private val viewModel: MainViewModel by viewModels(factoryProducer = {
        object : AbstractSavedStateViewModelFactory(this, null) {

            override fun <T : ViewModel> create(
                key: String,
                modelClass: Class<T>,
                handle: SavedStateHandle
            ): T {
                return if (modelClass == MainViewModel::class.java) {
                    val api = (application as App).retrofit.create(WeatherApi::class.java)
                    MainViewModel(api) as T
                } else {
                    throw ClassNotFoundException()
                }
            }

        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.getSupportActionBar()?.hide()

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), 3
            )
            return
        }

        locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        OverScrollDecoratorHelper.setUpOverScroll(binding.nestedScrollView)

        if (location != null && location.time > Calendar.getInstance().timeInMillis - 2 * 60 * 1000) {
            getCurrent(location.latitude, location.longitude)
            getForecast(location.latitude, location.longitude)
        } else {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, this);
        }

        binding.refresh.setOnRefreshListener {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 50f, this);

        }



        setupRecyclerView()
    }

    private fun getCurrent(lat: Double, lon: Double) {
        viewModel.getCurrentWeather(lat, lon)
        viewModel.currentWeatherResponse.observe(this, Observer
        { response ->
            if (response != null) {
                with(binding) {
                    refresh.isRefreshing = false

                    // Pressure calculation
                    val pressure = response.main.grndLevel.div(1.333)
                        .let { round(it).toInt() }
                    pressureValue.text = pressure.toString()

                    cityName.text = response.name
                    temp.text = getTemperatureString(binding.root, response.main.temp)
                    feelsTemp.text = getTemperatureString(binding.root, response.main.feelsLike)
                    windValue.text = response.wind.speed.toString()

                    windIcon.setImageDrawable(AppCompatResources.getDrawable(this@MainActivity, getWindIcon(response.wind.deg)))

                  Picasso.get()
                        .load(
                            response.weather[0].let { String.format(ICON_URL, it.icon) })
                        .into(binding.image)
                }
            }
        })
    }

    private fun getForecast(lat: Double, lon: Double) {
        viewModel.getForecastWeather(lat, lon)
        viewModel.forecastWeatherResponse.observe(this, Observer
        { response ->
            if (response != null) {
                forecastRowAdapter.setData(response.list)
            }

        })
    }

    private fun setupRecyclerView() {
        binding.recyclerViewParent.adapter = forecastRowAdapter
        binding.recyclerViewParent.layoutManager = horizontalLayoutManager
        binding.recyclerViewParent.isNestedScrollingEnabled = false
        binding.recyclerViewParent.setHasFixedSize(false)
    }

    override fun onLocationChanged(location: Location) {
            getCurrent(location.latitude, location.longitude)
            getForecast(location.latitude, location.longitude)
            locationManager.removeUpdates(this)
    }
}
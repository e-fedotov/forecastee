package ru.evgenyfedotov.weather

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AbstractSavedStateViewModelFactory
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

private const val LOCATION_PERMISSION_REQUEST_CODE = 3
private const val LOCATION_FRESHNESS_MS = 2 * 60 * 1000L

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
        supportActionBar?.hide()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        OverScrollDecoratorHelper.setUpOverScroll(binding.nestedScrollView)
        setupRecyclerView()
        observeViewModel()

        if (!hasLocationPermission()) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        initLocationAndFetch()

        binding.refresh.setOnRefreshListener {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 50f, this)
        }
    }

    private fun hasLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initLocationAndFetch()
                binding.refresh.setOnRefreshListener {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 50f, this)
                }
            } else {
                Toast.makeText(this, R.string.location_permission_denied, Toast.LENGTH_LONG).show()
            }
        }
    }

    @Suppress("MissingPermission")
    private fun initLocationAndFetch() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

        if (location != null && location.time > Calendar.getInstance().timeInMillis - LOCATION_FRESHNESS_MS) {
            getCurrent(location.latitude, location.longitude)
            getForecast(location.latitude, location.longitude)
        } else {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, this)
        }
    }

    private fun observeViewModel() {
        viewModel.currentWeatherResponse.observe(this) { response ->
            if (response != null) {
                with(binding) {
                    refresh.isRefreshing = false

                    val pressure = response.main.grndLevel.div(1.333).let { round(it).toInt() }
                    pressureValue.text = pressure.toString()

                    cityName.text = response.name
                    temp.text = getTemperatureString(binding.root, response.main.temp)
                    feelsTemp.text = getTemperatureString(binding.root, response.main.feelsLike)
                    windValue.text = response.wind.speed.toString()

                    windIcon.setImageDrawable(
                        AppCompatResources.getDrawable(this@MainActivity, getWindIcon(response.wind.deg))
                    )

                    Picasso.get()
                        .load(response.weather[0].let { String.format(ICON_URL, it.icon) })
                        .into(binding.image)
                }
            }
        }

        viewModel.forecastWeatherResponse.observe(this) { response ->
            if (response != null) {
                forecastRowAdapter.setData(response.list)
            }
        }

        viewModel.error.observe(this) { message ->
            if (message != null) {
                binding.refresh.isRefreshing = false
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun getCurrent(lat: Double, lon: Double) {
        viewModel.getCurrentWeather(lat, lon)
    }

    private fun getForecast(lat: Double, lon: Double) {
        viewModel.getForecastWeather(lat, lon)
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

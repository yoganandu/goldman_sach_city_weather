package uk.co.yoganandu.cityweather.weatherdetail


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import uk.co.yoganandu.cityweather.R
import uk.co.yoganandu.cityweather.WeatherApplication
import uk.co.yoganandu.cityweather.WeatherBaseFragment
import uk.co.yoganandu.cityweather.databinding.FragmentWeatherBinding
import uk.co.yoganandu.cityweather.db.CityDetails
import uk.co.yoganandu.cityweather.model.WeatherInfo
import uk.co.yoganandu.cityweather.utils.WeatherUtils
import kotlin.math.roundToInt


class WeatherFragment : WeatherBaseFragment(), WeatherDetailContract.View {


    private var _binding: FragmentWeatherBinding? = null

    private val binding get() = _binding!!

    private lateinit var weatherDetailPresenter: WeatherDetailPresenter
    private  var cityId: String? = null
    private var cityDetail : CityDetails? = null


    private lateinit var locationManager: LocationManager


    private var isNetworkEnabled = false
    private var isGpsEnabled = false


    private val requestLocationPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                requestCurrentLocation()
            }
        }


    private val locationListener = LocationListener{
        removeLocationListener()
        weatherDetailPresenter.fetchCityWeatherByLatLong(
            it.latitude,
            it.longitude,
            Schedulers.io(),
            AndroidSchedulers.mainThread()
        )
    }

    private fun removeLocationListener(){
        locationManager.removeUpdates(locationListener)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        context?.let {
            locationManager = it.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        }
        cityId = arguments?.getString("cityId") ?: null
        weatherDetailPresenter = WeatherDetailPresenter(this)
        binding.btnAddFavourite.setOnClickListener {
            cityDetail?.let {
                val weatherApplication: WeatherApplication = requireActivity().application as WeatherApplication
                val weatherDao = weatherApplication.getWeatherDatabase().favouriteDao()
                lifecycleScope.launch (Dispatchers.Default){
                    weatherDao.addCityToFavourite(it)
                    withContext(Dispatchers.Main){
                        binding.btnRemoveFavourite.visibility = View.VISIBLE
                        binding.btnAddFavourite.visibility = View.GONE
                        setFragmentResult("requestKey", bundleOf("bundleKey" to "result"))
                    }
                }
            }
        }
        binding.btnRemoveFavourite.setOnClickListener {
            cityDetail?.let {
                val weatherApplication: WeatherApplication = requireActivity().application as WeatherApplication
                val weatherDao = weatherApplication.getWeatherDatabase().favouriteDao()
                lifecycleScope.launch(Dispatchers.Default) {
                    weatherDao.removeCityFromFavourite(it)
                    withContext(Dispatchers.Main){
                        binding.btnRemoveFavourite.visibility = View.GONE
                        binding.btnAddFavourite.visibility = View.VISIBLE
                        setFragmentResult("requestKey", bundleOf("bundleKey" to "result"))
                    }

                }
            }
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        when{
            cityId != null -> {
                weatherDetailPresenter.fetchCityWeatherById(
                        cityId!!,
                        Schedulers.io(),
                        AndroidSchedulers.mainThread())
            }
            !checkLocationPermission(requireActivity()) -> {
                requestCurrentLocationPermission(requireActivity())
            }
            else -> {
                requestCurrentLocation()
            }
        }
    }
    override fun showWeather(weatherInfo: WeatherInfo) {
        cityId = weatherInfo.cityId
        cityDetail = CityDetails(cityId = weatherInfo.cityId, cityName = weatherInfo.cityName)
        binding.llWeatherDetails.visibility = View.VISIBLE
        binding.tvDetailErrorMessage.visibility = View.GONE
        binding.tvcityName.text = weatherInfo.cityName + ", " + weatherInfo.sys.country
        binding.tvlastUpdate.text = WeatherUtils.getFormattedDateAndTimeFromUTC(weatherInfo.date)
        val weatherResourceStr = context?.getString(
            WeatherUtils.getWeatherImageResourceFromWeatherCode(
                weatherInfo.weather[0].icon
            )
        )
        binding.weatherDetailIconIV.setIconResource(weatherResourceStr)
        binding.currentTempDetailTV.text = weatherInfo.main.temp.roundToInt().toString() + " \u2103"
        binding.weatherConditionDetailTV.text = weatherInfo.weather[0].main
        binding.tvWindDetails.text = getString(
            R.string.wind_speed_text,
            weatherInfo.wind.speed.toString(),
            weatherInfo.wind.deg.toString()
        )
        binding.tvPressureDetails.text = getString(
            R.string.pressure_speed_text,
            weatherInfo.main.pressure.toString()
        )
        binding.tvHumidityDetails.text = getString(
            R.string.humidity_text,
            weatherInfo.main.humidity.toString()
        ) + " %"
        binding.tvSunriseDetails.text = getString(
            R.string.sunrise_text, WeatherUtils.getFormattedTimeFromUTC(
                weatherInfo.sys.sunrise
            )
        )
        binding.tvSunsetDetails.text = getString(
            R.string.sunrise_text, WeatherUtils.getFormattedTimeFromUTC(
                weatherInfo.sys.sunset
            )
        )
        enableFavroutieActions()
    }

    private fun enableFavroutieActions(){
        cityDetail?.let {
            val weatherApplication: WeatherApplication = requireActivity().application as WeatherApplication
            val weatherDao = weatherApplication.getWeatherDatabase().favouriteDao()
            weatherDao.getCityById(it.cityId).observe(viewLifecycleOwner, { cityDetails ->
                if(cityDetails != null){
                    binding.btnRemoveFavourite.visibility = View.VISIBLE
                    binding.btnAddFavourite.visibility = View.GONE
                }else{
                    binding.btnRemoveFavourite.visibility = View.GONE
                    binding.btnAddFavourite.visibility = View.VISIBLE
                }
            })
        }

    }
    override fun showError(errorTitle: String, errorMessage: String) {
        binding.llWeatherDetails.visibility = View.GONE
        binding.tvDetailErrorMessage.visibility = View.VISIBLE
        binding.tvDetailErrorMessage.text = errorMessage
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.weather_menu, menu);
        val searchMenuItem = menu.findItem(R.id.app_bar_search)
        val searchView: SearchView = searchMenuItem.actionView as SearchView
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    if(it.length > 3){
                        weatherDetailPresenter.fetchCityWeatherByName(
                            it,
                            Schedulers.io(),
                            AndroidSchedulers.mainThread())
                        searchView.setQuery("", false)
                        searchView.isIconified = true
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.app_bar_current_location -> {
                if (checkLocationPermission(requireActivity())) {
                    requestCurrentLocation()
                } else {
                    requestCurrentLocationPermission(requireActivity())
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun requestCurrentLocationPermission(activity: Activity){
        requestLocationPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
    }

    @SuppressLint("MissingPermission")
    private fun requestCurrentLocation(){
        val locationProvider = when{
            isGpsEnabled -> LocationManager.GPS_PROVIDER
            isNetworkEnabled -> LocationManager.NETWORK_PROVIDER
            else -> LocationManager.PASSIVE_PROVIDER
        }
        locationManager.requestLocationUpdates(
            locationProvider,
            5000,
            0F,
            locationListener
        )
    }

    private fun checkLocationPermission(activity: Activity): Boolean {
        return ActivityCompat.checkSelfPermission(
            activity,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

}

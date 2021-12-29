package uk.co.yoganandu.cityweather.weathers

import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.app_toolbar.*
import uk.co.yoganandu.cityweather.R
import uk.co.yoganandu.cityweather.WeatherApplication
import uk.co.yoganandu.cityweather.weatherdetail.WeatherFragment

class WeatherActivity : AppCompatActivity(), WeatherListFragment.WeatherCityClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
        setSupportActionBar(toolbarView)
        setUpDrawer()
        val weatherApplication = application as WeatherApplication
        weatherApplication.resetLud()
        val ft = supportFragmentManager.beginTransaction()
        ft.add(R.id.weatherFragment, WeatherFragment(), "weather_detail_fragment")
        ft.commit()
    }

    override fun showDetailedWeatherForCity(cityId: String) {
        val ft = supportFragmentManager.beginTransaction()
        supportFragmentManager.findFragmentByTag("weather_detail_fragment")?.let {
            ft.remove(it)
        }
        val weatherFragment = WeatherFragment()
        val bundle = Bundle()
        bundle.putString("cityId", cityId)
        weatherFragment.arguments = bundle
        ft.replace(R.id.weatherFragment, weatherFragment, "weather_detail_fragment")
        ft.commit()
    }

    private fun setUpDrawer() {
        val mDrawerToggle = ActionBarDrawerToggle(this, drawer_layout, toolbarView, R.string.drawer_open, R.string.drawer_close)
        drawer_layout.addDrawerListener(mDrawerToggle)
        drawer_layout.post { mDrawerToggle.syncState() }
    }
}

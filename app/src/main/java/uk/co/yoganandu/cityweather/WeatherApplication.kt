package uk.co.yoganandu.cityweather

import android.app.Application
import androidx.room.Room
import uk.co.yoganandu.cityweather.db.WeatherDatabase
import uk.co.yoganandu.cityweather.model.WeatherInfo

class WeatherApplication: Application() {

    private lateinit var weatherMap: HashMap<String, WeatherInfo>
    private  var lud: Long = 0
    private lateinit var weatherList: List<WeatherInfo>

    override fun onCreate() {
        super.onCreate()
        weatherMap = HashMap<String, WeatherInfo>()
    }
    private fun setLud(lud: Long){
        this.lud = lud
    }

    fun getWeatherDatabase(): WeatherDatabase {
        return Room.databaseBuilder(
                applicationContext,
                WeatherDatabase::class.java, "weather_favourite_list"
        ).build()
    }


    fun isWeatherDataExpired(): Boolean{
        return System.currentTimeMillis() - lud > 1800000
    }
    fun addWeatherList(weatherList: List<WeatherInfo>){
        this.weatherList = weatherList
        weatherMap.clear()
        for(weatherInfo in weatherList){
            weatherMap.put(weatherInfo.cityId, weatherInfo)
        }
        setLud(System.currentTimeMillis())
    }

    fun getWeatherDetails(cityId: Int): WeatherInfo?{
        return weatherMap.get(cityId)
    }

    fun getWeatherList(): List<WeatherInfo>{
        return weatherList
    }

    fun resetLud(){
        setLud(0)
    }
}
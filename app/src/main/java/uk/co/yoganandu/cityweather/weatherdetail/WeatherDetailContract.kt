package uk.co.yoganandu.cityweather.weatherdetail

import io.reactivex.Scheduler
import uk.co.yoganandu.cityweather.model.WeatherInfo

interface WeatherDetailContract{

    interface View{
        fun showProgress()
        fun hideProgress()
        fun showWeather(weather: WeatherInfo)
        fun showError(errorTitle : String, errorMessage : String)
    }

    interface Presenter{
        fun fetchCityWeatherById(cityId: String, processScheduler: Scheduler, androidScheduler: Scheduler)
        fun fetchCityWeatherByName(cityName: String, processScheduler: Scheduler, androidScheduler: Scheduler)
        fun fetchCityWeatherByLatLong(lat: Double, long: Double, processScheduler: Scheduler, androidScheduler: Scheduler)
    }
}
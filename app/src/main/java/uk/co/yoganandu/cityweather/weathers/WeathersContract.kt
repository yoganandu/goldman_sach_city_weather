package uk.co.yoganandu.cityweather.weathers

import io.reactivex.Scheduler
import uk.co.yoganandu.cityweather.model.WeatherInfo

interface WeathersContract{

    interface View{
        fun showProgress()
        fun hideProgress()
        fun showWeatherList(weatherList: List<WeatherInfo>)
        fun showError(errorTitle : String, errorMessage : String)
    }

    interface Presenter{
        fun fetchWeathers(cityIds: List<String>, processScheduler: Scheduler, androidScheduler: Scheduler)
    }
}
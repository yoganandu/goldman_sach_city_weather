package uk.co.yoganandu.cityweather.data

import io.reactivex.Scheduler
import uk.co.yoganandu.cityweather.api.WeatherAPICallBack

interface WeatherRepository{

    fun fetchWeatherList(cityIds: String,
                         weathersListCallBack: WeatherAPICallBack.WeathersListCallBack,
                         processSecheduler: Scheduler, androidSchedulers: Scheduler)
    fun fetchWeatherByCityName(cityName: String, weatherInfoCallBack: WeatherAPICallBack.WeatherCallBack,
                               processSecheduler: Scheduler, androidSchedulers: Scheduler)
    fun fetchWeatherByLatLong(lat: Double, long: Double, weatherInfoCallBack: WeatherAPICallBack.WeatherCallBack,
                              processSecheduler: Scheduler, androidSchedulers: Scheduler)
    fun fetchWeatherByCityId(cityId: String, weatherInfoCallBack: WeatherAPICallBack.WeatherCallBack,
                             processSecheduler: Scheduler, androidSchedulers: Scheduler)
}
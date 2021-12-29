package uk.co.yoganandu.cityweather.api

import uk.co.yoganandu.cityweather.model.WeatherAPIError
import uk.co.yoganandu.cityweather.model.WeatherInfo

interface WeatherAPICallBack{
    interface WeathersListCallBack{
        fun onSuccess(weatherList: List<WeatherInfo>)
        fun onError(weatherError: WeatherAPIError)
    }
    interface WeatherCallBack{
        fun onSuccess(weatherInfo: WeatherInfo)
        fun onError(weatherError: WeatherAPIError)
    }
}
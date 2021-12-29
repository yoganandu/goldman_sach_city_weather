package uk.co.yoganandu.cityweather.weathers

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import uk.co.yoganandu.cityweather.api.WeatherAPICallBack
import uk.co.yoganandu.cityweather.data.WeatherAPIRepository
import uk.co.yoganandu.cityweather.model.WeatherAPIError
import uk.co.yoganandu.cityweather.model.WeatherInfo
import uk.co.yoganandu.cityweather.utils.WeatherUtils

class WeathersPresenter(val weatherListView: WeathersContract.View) : WeathersContract.Presenter{


    override fun fetchWeathers(cityIds: List<String>, processScheduler: Scheduler, androidScheduler: Scheduler) {
        weatherListView.showProgress()
        val weatherRepository = WeatherAPIRepository()
        weatherRepository.fetchWeatherList(WeatherUtils.getValidCommaSeperatedStringFromStringsArray(cityIds), object: WeatherAPICallBack.WeathersListCallBack {
            override fun onSuccess(weatherList: List<WeatherInfo>) {
                weatherListView.hideProgress()
                weatherListView.showWeatherList(weatherList)
            }

            override fun onError(weatherError: WeatherAPIError) {
                weatherListView.hideProgress()
                weatherListView.showError(weatherError.errorCode.toString(), weatherError.errorMessage)
            }

        }, processScheduler, androidScheduler)
    }
}
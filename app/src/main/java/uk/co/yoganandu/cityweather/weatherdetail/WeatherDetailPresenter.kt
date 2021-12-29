package uk.co.yoganandu.cityweather.weatherdetail

import io.reactivex.Scheduler
import uk.co.yoganandu.cityweather.api.WeatherAPICallBack
import uk.co.yoganandu.cityweather.data.WeatherAPIRepository
import uk.co.yoganandu.cityweather.model.WeatherAPIError
import uk.co.yoganandu.cityweather.model.WeatherInfo

class WeatherDetailPresenter(val weatherView: WeatherDetailContract.View): WeatherDetailContract.Presenter{
    override fun fetchCityWeatherById(cityId: String, processScheduler: Scheduler, androidScheduler: Scheduler) {
        val weatherRepository = WeatherAPIRepository()
        weatherRepository.fetchWeatherByCityId(cityId, object: WeatherAPICallBack.WeatherCallBack {
            override fun onSuccess(weatherInfo: WeatherInfo) {
                weatherView.hideProgress()
                weatherView.showWeather(weatherInfo)
            }

            override fun onError(weatherError: WeatherAPIError) {
                weatherView.hideProgress()
                weatherView.showError(weatherError.errorCode.toString(), weatherError.errorMessage)
            }

        }, processScheduler, androidScheduler)
    }

    override fun fetchCityWeatherByName(cityName: String, processScheduler: Scheduler, androidScheduler: Scheduler) {
        weatherView.showProgress()
        val weatherRepository = WeatherAPIRepository()
        weatherRepository.fetchWeatherByCityName(cityName, object: WeatherAPICallBack.WeatherCallBack {
            override fun onSuccess(weatherInfo: WeatherInfo) {
                weatherView.hideProgress()
                weatherView.showWeather(weatherInfo)
            }

            override fun onError(weatherError: WeatherAPIError) {
                weatherView.hideProgress()
                weatherView.showError(weatherError.errorCode.toString(), weatherError.errorMessage)
            }

        }, processScheduler, androidScheduler)
    }

    override fun fetchCityWeatherByLatLong(lat: Double, long: Double, processScheduler: Scheduler, androidScheduler: Scheduler) {
        weatherView.showProgress()
        val weatherRepository = WeatherAPIRepository()
        weatherRepository.fetchWeatherByLatLong(lat, long, object: WeatherAPICallBack.WeatherCallBack {
            override fun onSuccess(weatherInfo: WeatherInfo) {
                weatherView.hideProgress()
                weatherView.showWeather(weatherInfo)
            }

            override fun onError(weatherError: WeatherAPIError) {
                weatherView.hideProgress()
                weatherView.showError(weatherError.errorCode.toString(), weatherError.errorMessage)
            }

        }, processScheduler, androidScheduler)
    }

}
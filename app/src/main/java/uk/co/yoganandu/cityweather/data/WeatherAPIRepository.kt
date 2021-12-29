package uk.co.yoganandu.cityweather.data

import android.util.Log
import com.google.gson.GsonBuilder
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import uk.co.yoganandu.cityweather.api.OpenWeatherAPIService
import uk.co.yoganandu.cityweather.api.WeatherAPICallBack
import uk.co.yoganandu.cityweather.api.WeatherAPIConstants
import uk.co.yoganandu.cityweather.model.WeatherAPIError
import uk.co.yoganandu.cityweather.model.WeatherAPIResponse
import uk.co.yoganandu.cityweather.model.WeatherInfo

class WeatherAPIRepository: WeatherRepository{

    override fun fetchWeatherByCityId(cityId: String, weatherInfoCallBack: WeatherAPICallBack.WeatherCallBack, processSecheduler: Scheduler, androidSchedulers: Scheduler) {
        val weatherAPIService: OpenWeatherAPIService = OpenWeatherAPIService.create()
        weatherAPIService.getCurrentWeatherForCityById(WeatherAPIConstants.API_KEY, cityId,
            WeatherAPIConstants.UNITS_METRIC).observeOn(androidSchedulers)
            .subscribeOn(processSecheduler).subscribe(WeatherDisposableObserver(weatherInfoCallBack))
    }

    override fun fetchWeatherList(cityIds: String, weathersListCallBack: WeatherAPICallBack.WeathersListCallBack,
                                  processSecheduler: Scheduler, androidSchedulers: Scheduler) {
        val weatherAPIService: OpenWeatherAPIService = OpenWeatherAPIService.create()
        weatherAPIService.getCurrentWeatherForSeveralCities(WeatherAPIConstants.API_KEY,cityIds,
                WeatherAPIConstants.UNITS_METRIC).observeOn(androidSchedulers)
                .subscribeOn(processSecheduler).subscribe(object: DisposableObserver<WeatherAPIResponse>() {
                    override fun onComplete() {
                        Log.d("RxWeather","OnComplete()")
                    }

                    override fun onNext(t: WeatherAPIResponse) {
                        Log.d("RxWeather","OnNext")
                        weathersListCallBack.onSuccess(t.weatherList)
                    }

                    override fun onError(e: Throwable) {
                        lateinit var apiError: WeatherAPIError
                        if(e is HttpException){
                            val gson = GsonBuilder().create()
                            apiError = gson.fromJson(e.response()?.errorBody()?.string(), WeatherAPIError::class.java)
                        }else{
                            apiError = WeatherAPIError(-1, e.message!!)
                        }
                        weathersListCallBack.onError(apiError)
                        Log.d("RxWeather","OnError() - " + apiError.errorCode + " " + apiError.errorMessage)
                    }
                })
    }

    override fun fetchWeatherByCityName(cityName: String, weatherInfoCallBack: WeatherAPICallBack.WeatherCallBack, processSecheduler: Scheduler, androidSchedulers: Scheduler) {
        val weatherAPIService: OpenWeatherAPIService = OpenWeatherAPIService.create()
        weatherAPIService.getCurrentWeatherForCityByName(WeatherAPIConstants.API_KEY, cityName,
                WeatherAPIConstants.UNITS_METRIC).observeOn(androidSchedulers)
                .subscribeOn(processSecheduler).subscribe(WeatherDisposableObserver(weatherInfoCallBack))
    }

    override fun fetchWeatherByLatLong(lat: Double, long: Double, weatherInfoCallBack: WeatherAPICallBack.WeatherCallBack, processSecheduler: Scheduler, androidSchedulers: Scheduler) {
        val weatherAPIService: OpenWeatherAPIService = OpenWeatherAPIService.create()
        weatherAPIService.getCurrentWeatherForCityByLatLong(WeatherAPIConstants.API_KEY, lat, long,
                WeatherAPIConstants.UNITS_METRIC).observeOn(androidSchedulers)
                .subscribeOn(processSecheduler).subscribe(WeatherDisposableObserver(weatherInfoCallBack))
    }

}

class WeatherDisposableObserver(private val weatherInfoCallBack: WeatherAPICallBack.WeatherCallBack): DisposableObserver<WeatherInfo>() {
    override fun onComplete() {
        Log.d("RxWeather","OnComplete()")
    }

    override fun onNext(t: WeatherInfo) {
        Log.d("RxWeather","OnNext")
        weatherInfoCallBack.onSuccess(t)
    }

    override fun onError(e: Throwable) {
        lateinit var apiError: WeatherAPIError
        apiError = if(e is HttpException){
            val gson = GsonBuilder().create()
            gson.fromJson(e.response()?.errorBody()?.string(), WeatherAPIError::class.java)
        }else{
            WeatherAPIError(-1, e.message!!)
        }
        weatherInfoCallBack.onError(apiError)
        Log.d("RxWeather","OnError() - " + apiError.errorCode + " " + apiError.errorMessage)
    }

}
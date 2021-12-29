package uk.co.yoganandu.cityweather

import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import uk.co.yoganandu.cityweather.weatherdetail.WeatherDetailContract
import uk.co.yoganandu.cityweather.weatherdetail.WeatherDetailPresenter

class WeatherPresenterTest{
    private val weatherView: WeatherDetailContract.View = Mockito.mock(WeatherDetailContract.View::class.java)
    private val objectUnderTest = WeatherDetailPresenter(weatherView)

    private fun <T> any(): T {
        Mockito.any<T>()
        return uninitialized()
    }
    private fun <T> uninitialized(): T = null as T

    @Before
    fun setUP(){
        MockitoAnnotations.initMocks(this)

    }
    @Test
    fun testFetchWeatherForInValidCityName(){
        val testScheduler = TestScheduler()
        objectUnderTest.fetchCityWeatherByName("test name", testScheduler, testScheduler)
        testScheduler.triggerActions()
        Mockito.verify(weatherView).showProgress()
        Mockito.verify(weatherView).showError(any(),any())
        Mockito.verify(weatherView).hideProgress()
        Mockito.verifyNoMoreInteractions(weatherView)
    }
    @Test
    fun testShowListIsNotCalledForInValidCityName(){
        val testScheduler = TestScheduler()
        objectUnderTest.fetchCityWeatherByName("test name", testScheduler, testScheduler)
        testScheduler.triggerActions()
        Mockito.verify(weatherView, Mockito.never()).showWeather(any())
    }
    @Test
    fun testFetchWeatherForValidCityName(){
        val testScheduler = TestScheduler()
        objectUnderTest.fetchCityWeatherByName("test name", testScheduler, testScheduler)
        testScheduler.triggerActions()
        Mockito.verify(weatherView).showProgress()
        Mockito.verify(weatherView).showWeather(any())
        Mockito.verify(weatherView).hideProgress()
        Mockito.verifyNoMoreInteractions(weatherView)
    }
    @Test
    fun testShowErrorIsNotCalledForValidCityName(){
        val testScheduler = TestScheduler()
        //objectUnderTest.fetchCityWeatherById(2643743, testScheduler, testScheduler)
        objectUnderTest.fetchCityWeatherByName("Ilford", testScheduler, testScheduler)
        testScheduler.triggerActions()
        Mockito.verify(weatherView, Mockito.never()).showError(any(), any())
    }
}
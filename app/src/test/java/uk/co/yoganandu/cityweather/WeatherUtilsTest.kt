package uk.co.yoganandu.cityweather

import org.junit.Assert
import org.junit.Test
import uk.co.yoganandu.cityweather.utils.WeatherUtils

class WeatherUtilsTest{
    @Test
    fun checkWithSingleCityId() {
        Assert.assertEquals("1234",
                WeatherUtils.getValidCommaSeperatedStringFromStringsArray(listOf("1234")))
    }
    @Test
    fun checkWithMultipleCityIds() {
        Assert.assertEquals("1234,5678",
                WeatherUtils.getValidCommaSeperatedStringFromStringsArray(listOf("1234", "5678")))
    }
    @Test
    fun checkWithBlankCityIds() {
        Assert.assertEquals("",
                WeatherUtils.getValidCommaSeperatedStringFromStringsArray(listOf()))
    }
    @Test
    fun checkWithSingleCityIdWithComma() {
        Assert.assertEquals("",
                WeatherUtils.getValidCommaSeperatedStringFromStringsArray(listOf("1234,")))
    }
    @Test
    fun checkWithSingleCityIdWithInvalidFormat() {
        Assert.assertEquals("",
                WeatherUtils.getValidCommaSeperatedStringFromStringsArray(listOf("SingleCityId")))
    }

    @Test
    fun checkValidWeatherResouce(){
        Assert.assertEquals(R.string.wi_day_cloudy,
                WeatherUtils.getWeatherImageResourceFromWeatherCode("03d"))
    }
    @Test
    fun checkInValidWeatherResouce(){
        Assert.assertEquals(R.string.wi_day_sunny,
                WeatherUtils.getWeatherImageResourceFromWeatherCode("30d"))
    }
}
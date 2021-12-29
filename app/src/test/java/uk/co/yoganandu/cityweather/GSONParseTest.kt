package uk.co.yoganandu.cityweather

import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import org.junit.Assert
import org.junit.Test
import uk.co.yoganandu.cityweather.model.WeatherInfo

class GSONParseTest{
    @Test
    fun testWeatherInfoParse(){
        val weatherInfoJSONStr = "{\"coord\":{\"lon\":-0.13,\"lat\":51.51},\"weather\":[{\"id\":500,\"main\":\"Rain\"," +
                "\"description\":\"light rain\",\"icon\":\"10n\"}],\"base\":\"stations\",\"main\":{\"temp\":4.65,\"pressure\":1016," +
                "\"humidity\":61,\"temp_min\":1,\"temp_max\":8},\"visibility\":10000,\"wind\":{\"speed\":2.6,\"deg\":300}," +
                "\"rain\":{\"3h\":0.175},\"clouds\":{\"all\":76},\"dt\":1522034400,\"sys\":{\"type\":1,\"id\":5091,\"message\":0.006," +
                "\"country\":\"GB\",\"sunrise\":1522043368,\"sunset\":1522088629},\"id\":2643743,\"name\":\"London\",\"cod\":200}"
        val gsonBuilder = GsonBuilder().create()
        var parseSuccess = false
        parseSuccess = try{
            gsonBuilder.fromJson(weatherInfoJSONStr, WeatherInfo::class.java)
            true
        }catch (e: JsonSyntaxException){
            e.printStackTrace()
            false
        }

        Assert.assertEquals(true, parseSuccess)
    }
}
package uk.co.yoganandu.cityweather.utils

import android.util.Log
import uk.co.yoganandu.cityweather.R
import java.text.SimpleDateFormat
import java.util.*


class WeatherUtils{
    companion object {
        fun getValidCommaSeperatedStringFromStringsArray(stringArray: List<String>) : String{
            val validatedStringArray = stringArray
                                        .filter { !it.endsWith(",") }
                                        .filter { it.toIntOrNull() != null }
            return validatedStringArray.joinToString(",")
        }
        fun getWeatherImageResourceFromWeatherCode(weatherCode: String): Int{
            var weatherResourceId = R.string.wi_day_sunny
            when(weatherCode){
                "01d" -> weatherResourceId = R.string.wi_day_sunny
                "01n" -> weatherResourceId = R.string.wi_night_clear
                "02d", "03d", "04d" -> weatherResourceId = R.string.wi_day_cloudy
                "02n", "03n", "04n" -> weatherResourceId = R.string.wi_night_cloudy
                "09d" -> weatherResourceId = R.string.wi_day_showers
                "09n" -> weatherResourceId = R.string.wi_night_showers
                "10d" -> weatherResourceId = R.string.wi_day_rain
                "10n" -> weatherResourceId = R.string.wi_night_rain
                "11d" -> weatherResourceId = R.string.wi_day_thunderstorm
                "11n" -> weatherResourceId = R.string.wi_night_thunderstorm
                "13d" -> weatherResourceId = R.string.wi_day_snow
                "13n" -> weatherResourceId = R.string.wi_night_snow
                "50d" -> weatherResourceId = R.string.wi_day_fog
                "50n" -> weatherResourceId = R.string.wi_night_fog
            }
            return weatherResourceId
        }

        fun getFormattedTimeFromUTC(utcTime: Long): String{
            val formatDate = SimpleDateFormat("hh:mm a")
            formatDate.timeZone = TimeZone.getTimeZone("UTC")
            val date = Date(utcTime * 1000)
            return formatDate.format(date)
        }
        fun getFormattedDateAndTimeFromUTC(utcTime: Long): String{
            val formatDate = SimpleDateFormat("dd.MM.yyyy 'at' hh:mm a")
            formatDate.timeZone = TimeZone.getTimeZone("UTC")
            val date = Date(utcTime * 1000)
            return formatDate.format(date)
        }
    }
}
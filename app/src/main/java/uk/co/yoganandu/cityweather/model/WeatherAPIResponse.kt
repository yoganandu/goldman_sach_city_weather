package uk.co.yoganandu.cityweather.model

import com.google.gson.annotations.SerializedName

data class Coordinates(val lon: Double,
                       val lat: Double)

data class Weather(val id: Int, val main: String,
                   val description: String,
                   val icon: String)

data class Main(val temp: Double,
                val pressure: Int,
                val humidity: Int,
                val temp_min: Double,
                val temp_max: Double,
                val sea_level: Double,
                val grnd_level: Double)

data class Wind(val speed: Float,
                val deg: Double)

data class Clouds(val all: Int)

data class Rain(@SerializedName("3h") val last3h: Double)
data class Snow(@SerializedName("3h") val last3h: Int)
data class Sys(val type: Int,
               val id: Int,
               val message: Double,
               val country: String,
               val sunrise: Long,
               val sunset: Long)

data class WeatherInfo(val coord: Coordinates,
                       val weather: List<Weather>,
                       val base: String,
                       val main: Main,
                       val wind: Wind,
                       val clouds: Clouds,
                       val rain: Rain,
                       val snow: Snow,
                       @SerializedName("dt") val date: Long,
                       val sys: Sys,
                       @SerializedName("id") val cityId: String,
                       @SerializedName("name") val cityName: String,
                       @SerializedName("cod") val responseCode: Int)
data class WeatherAPIResponse(val count: Int,
                              @SerializedName("list") val weatherList: List<WeatherInfo>)
data class WeatherAPIError(@SerializedName("cod") val errorCode: Int,
                           @SerializedName("message") val errorMessage: String)
data class WeatherUIInfo(val weatherInfo: WeatherInfo,
                         val isFavourite: Boolean = false)
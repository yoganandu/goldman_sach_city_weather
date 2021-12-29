package uk.co.yoganandu.cityweather.weathers

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.github.pwittchen.weathericonview.WeatherIconView
import kotlinx.android.synthetic.main.weather_list_item.view.*
import uk.co.yoganandu.cityweather.R
import uk.co.yoganandu.cityweather.databinding.WeatherListItemBinding
import uk.co.yoganandu.cityweather.model.WeatherInfo
import uk.co.yoganandu.cityweather.utils.WeatherUtils

class WeatherListAdapter(private val weatherList:List<WeatherInfo>, private val context: Context
                         , private val itemClickListener: WeatherListFragment.WeatherCityClickListener)
    : RecyclerView.Adapter<WeatherListAdapter.WeatherViewHolder>(), View.OnClickListener {

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        with(holder){
            with(weatherList[position]){
                binding.cityNameTV.text = cityName
                binding.weatherConditionTV.text = weather[0].main
                binding.currentTempTV.text = Math.round(main.temp).toString() + "\u00B0"
                val weatherResourceStr = context.getString(WeatherUtils.getWeatherImageResourceFromWeatherCode(weather[0].icon))
                binding.weatherIconIV.setIconResource(weatherResourceStr)
                binding.root.setOnClickListener {
                    itemClickListener.showDetailedWeatherForCity(cityId)
                }
            }
        }
    }

    inner class WeatherViewHolder(val binding: WeatherListItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val binding = WeatherListItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return WeatherViewHolder(binding)
    }

    override fun getItemCount() = weatherList.size

    override fun onClick(view: View?) {
    }

}
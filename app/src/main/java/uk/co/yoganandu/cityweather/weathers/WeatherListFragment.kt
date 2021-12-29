package uk.co.yoganandu.cityweather.weathers

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import uk.co.yoganandu.cityweather.WeatherApplication
import uk.co.yoganandu.cityweather.WeatherBaseFragment
import uk.co.yoganandu.cityweather.databinding.FragmentWeatherListBinding
import uk.co.yoganandu.cityweather.model.WeatherInfo


class WeatherListFragment : WeatherBaseFragment(), WeathersContract.View {

    private var _binding: FragmentWeatherListBinding? = null
    private val binding get() = _binding!!

    private lateinit var mListener: WeatherCityClickListener

    private lateinit var mWeatherListPresenter: WeathersPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener("requestKey") { requestKey, bundle ->
            // We use a String here, but any type that can be put in a Bundle is supported
            val weatherApplication: WeatherApplication = activity?.application as WeatherApplication
            weatherApplication.getWeatherDatabase().favouriteDao().getAll().observe(viewLifecycleOwner, Observer {
                val cityList = it.map { cityDetails -> cityDetails.cityId }
                mWeatherListPresenter.fetchWeathers(cityList, Schedulers.io(), AndroidSchedulers.mainThread())
            })
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentWeatherListBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lvWeatherList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.lvWeatherList.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        mWeatherListPresenter = WeathersPresenter(this)
        val weatherApplication: WeatherApplication = activity?.application as WeatherApplication
        weatherApplication.getWeatherDatabase().favouriteDao().getAll().observe(viewLifecycleOwner, Observer {

            val cityList = it.map { cityDetails -> cityDetails.cityId }
            mWeatherListPresenter.fetchWeathers(cityList, Schedulers.io(), AndroidSchedulers.mainThread())
        })
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is WeatherCityClickListener) {
            mListener = context
        } else {
            throw RuntimeException(requireContext().toString() + " must implement WeatherCityClickListener")
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface WeatherCityClickListener {
        fun showDetailedWeatherForCity(cityId: String)
    }
    override fun showWeatherList(weatherList: List<WeatherInfo>) {
        val weatherApplication: WeatherApplication = activity?.application as WeatherApplication
        weatherApplication.addWeatherList(weatherList)
        val weatherListAdapter = WeatherListAdapter(weatherList, requireActivity(), mListener)
        binding.lvWeatherList.visibility = View.VISIBLE
        binding.lvWeatherList.adapter = weatherListAdapter
        weatherListAdapter.notifyDataSetChanged()
    }

    override fun showError(errorTitle: String, errorMessage: String) {
        binding.lvWeatherList.visibility = View.GONE
        binding.tvErrorMessage.text = errorMessage
    }

}

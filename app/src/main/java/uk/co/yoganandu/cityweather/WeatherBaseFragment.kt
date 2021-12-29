package uk.co.yoganandu.cityweather

import android.app.Dialog
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AlertDialog


open class WeatherBaseFragment: Fragment(){
    private var mDialog: Dialog? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setUpProgressDialog()
    }

    fun showProgress(){
        mDialog?.show()
    }
    fun hideProgress(){
        mDialog?.dismiss()
    }
    /**
     * This method sets up the progress dialog to be shown while the HTTP call is happening
     */
    private fun setUpProgressDialog(){
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(R.layout.weather_loading_progress_layout)
        builder.setCancelable(false)
        mDialog = builder.create()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

}
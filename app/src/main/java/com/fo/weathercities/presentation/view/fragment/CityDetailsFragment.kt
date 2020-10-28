package com.fo.weathercities.presentation.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.fo.weathercities.data.model.City
import com.fo.weathercities.databinding.FragmentCityDetailsBinding
import com.fo.weathercities.presentation.presenter.CityDetailsPresenter
import com.fo.weathercities.presentation.view.`interface`.BackPressInterface
import com.fo.weathercities.presentation.view.`interface`.CityDetailsView
import com.fo.weathercities.presentation.view.intent.BackIntent
import com.fo.weathercities.presentation.view.intent.ConvertInit
import com.fo.weathercities.presentation.view.intent.DetailsInit
import com.fo.weathercities.presentation.view.viewstate.CityDetailsViewState
import com.hannesdorfmann.mosby3.mvi.MviFragment
import com.jakewharton.rxbinding4.view.clicks
import hu.akarnokd.rxjava3.bridge.RxJavaBridge
import io.reactivex.Observable

const val ARG_PARAM = "city"

class CityDetailsFragment : MviFragment<CityDetailsView, CityDetailsPresenter>(),
    CityDetailsView, BackPressInterface {

    private var presenter = CityDetailsPresenter()
    private lateinit var binding: FragmentCityDetailsBinding
    private lateinit var city: City

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCityDetailsBinding.inflate(inflater)
        city = arguments?.getParcelable(ARG_PARAM)!!
        return binding.root
    }

    override fun createPresenter(): CityDetailsPresenter = presenter
    override fun init(): Observable<DetailsInit> = Observable.just(DetailsInit(city = city))

    override fun convert(): Observable<ConvertInit> =
        RxJavaBridge.toV2Observable(binding.cityDetailsFragmentConvert.clicks()).map { ConvertInit }

    override fun back(): Observable<BackIntent> = Observable.just(BackIntent);//binding.//Observable.just(BackIntent)

    companion object {
        fun newInstance(city: City): CityDetailsFragment {
            val fragment = CityDetailsFragment()
            val args = Bundle()
            args.putParcelable(ARG_PARAM, city)
            fragment.arguments = args
            return fragment
        }
    }

    override fun render(viewState: CityDetailsViewState) {
        isLoading(viewState.loading)
        setData(viewState.city)
        isError(viewState.error)
    }

    private fun isLoading(loading: Boolean) = if (loading) {
        binding.cityDetailsFragmentLoadingIndicator.visibility = VISIBLE
    } else {
        binding.cityDetailsFragmentLoadingIndicator.visibility = GONE
    }

    private fun setData(city: City?) {
        city?.let {
            binding.apply {
                val today = "Today ${city.lowTemperature} - ${city.highTemperature}"
                val currentTemp = "Currently ${city.temperature}"
                cityDetailsFragmentLayout.visibility = VISIBLE
                cityDetailsFragmentName.text = city.city
                cityDetailsFragmentDegree.text = currentTemp
                cityDetailsFragmentSky.text = city.weatherDescription
                cityDetailsFragmentToday.text = today
            }

        }
    }

    private fun isError(error: Boolean) = if (error) {
        binding.cityDetailsFragmentErrorState.visibility = VISIBLE
    } else {
        binding.cityDetailsFragmentErrorState.visibility = GONE
    }

    override fun onBackPressed(): Boolean {
        return true
    }
}
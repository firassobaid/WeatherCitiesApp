package com.fo.weathercities.presentation.presenter

import android.util.Log
import com.fo.weathercities.data.model.ApiWeather
import com.fo.weathercities.data.model.City
import com.fo.weathercities.data.service.ApiHelperImpl
import com.fo.weathercities.data.service.WeatherService.buildService
import com.fo.weathercities.presentation.view.`interface`.CityDetailsView
import com.fo.weathercities.presentation.view.mutation.*
import com.fo.weathercities.presentation.view.viewstate.CityDetailsViewState
import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

const val CITY_DETAILS_PRESENTER_TAG = "CityDetailsPresenter"

class CityDetailsPresenter : MviBasePresenter<CityDetailsView, CityDetailsViewState>() {

    private val apiWeather = buildService()
    private val api = ApiHelperImpl(apiWeather)

    override fun bindIntents() {
        val init: Observable<CityDetailsMutation> =
            intent { it.init() }
                .switchMap { intent ->
                    val city = intent.city
                    api.getWeather(city.id)
                        .map<CityDetailsMutation> { result ->
                            CityDetailsInitMutation(city, result)
                        }
                        .subscribeOn(Schedulers.io())
                }
                .doOnError {
                    Log.e(CITY_DETAILS_PRESENTER_TAG, "Error initializing city details - $it")
                }
                .onErrorReturnItem(CityDetailsErrorMutation)
                .observeOn(AndroidSchedulers.mainThread())

        val convert: Observable<CityDetailsMutation> = intent { it.convert() }
            .map { CityDetailsConvertMutation }

        val state = init.mergeWith(convert)
            .scan<CityDetailsViewState>(CityDetailsViewState(loading = true), this::reduce)
            .observeOn(AndroidSchedulers.mainThread())
            .distinctUntilChanged()
            .subscribeOn(Schedulers.io())

        subscribeViewState(state, CityDetailsView::render)
    }

    private fun reduce(
        oldState: CityDetailsViewState,
        mutation: CityDetailsMutation
    ): CityDetailsViewState {
        return when (mutation) {
            is CityDetailsInitMutation -> oldState.copy(
                loading = false,
                error = false,
                city = convertApiWeatherToDisplay(mutation.city, mutation.result)
            )
            is CityDetailsConvertMutation -> oldState.copy(
                loading = false,
                error = false,
                city = updateCityTemperatures(oldState.city, oldState.isCelsius),
                isCelsius = !oldState.isCelsius
            )

            CityDetailsErrorMutation -> oldState.copy(error = true, loading = false)
            CityDetailsLoadingMutation -> oldState.copy(loading = true)
        }
    }

    private fun convertApiWeatherToDisplay(oldCity: City, apiWeather: ApiWeather): City {
        return apiWeather.weather?.let {
            City(
                weatherDescription = apiWeather.weather.first().description,
                weatherIcon = apiWeather.weather.first().icon,
                temperature = apiWeather.main?.temp?.substringBefore("."),
                lowTemperature = apiWeather.main?.temp_min?.substringBefore("."),
                highTemperature = apiWeather.main?.temp_max?.substringBefore("."),
                id = oldCity.id,
                city = oldCity.city
            )
        } ?: oldCity
    }

    private fun updateCityTemperatures(
        city: City?,
        celsius: Boolean
    ): City? {
        return city?.let {
            val temperature: String
            val highTemperature: String
            val lowTemperature: String
            if (celsius) {
                temperature = convertCelsiusToFahrenheit(it.temperature)
                highTemperature = convertCelsiusToFahrenheit(it.highTemperature)
                lowTemperature = convertCelsiusToFahrenheit(it.lowTemperature)
            } else {
                temperature = convertFahrenheitToCelsius(it.temperature)
                highTemperature = convertFahrenheitToCelsius(it.highTemperature)
                lowTemperature = convertFahrenheitToCelsius(it.lowTemperature)
            }
            it.copy(
                temperature = temperature,
                highTemperature = highTemperature,
                lowTemperature = lowTemperature
            )
        } ?: city
    }

    private fun convertCelsiusToFahrenheit(temp: String?): String =
        ((temp?.toDouble() ?: 0.0 * 9 / 5) + 32).toInt().toString()

    private fun convertFahrenheitToCelsius(temp: String?): String =
        ((temp?.toDouble() ?: 0.0 * 5 / 9) - 32).toInt().toString()
}
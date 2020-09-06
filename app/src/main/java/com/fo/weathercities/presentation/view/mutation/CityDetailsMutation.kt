package com.fo.weathercities.presentation.view.mutation

import com.fo.weathercities.data.model.ApiWeather
import com.fo.weathercities.data.model.City

sealed class CityDetailsMutation
data class CityDetailsInitMutation(
    val city: City,
    val result: ApiWeather
) : CityDetailsMutation()

object CityDetailsConvertMutation : CityDetailsMutation()
object CityDetailsErrorMutation : CityDetailsMutation()
object CityDetailsLoadingMutation : CityDetailsMutation()
package com.fo.weathercities.presentation.view.mutation

import com.fo.weathercities.data.model.City

sealed class CitiesMutation
data class CitiesInitMutation(val cities: List<City>) : CitiesMutation()
data class CityClickMutation(val city: City?) : CitiesMutation()
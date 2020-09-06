package com.fo.weathercities.presentation.view.viewstate

import com.fo.weathercities.data.model.City

data class CitiesViewState(
    val loading: Boolean = false,
    val cities: List<City> = emptyList(),
    val cityClicked: City? = null
)
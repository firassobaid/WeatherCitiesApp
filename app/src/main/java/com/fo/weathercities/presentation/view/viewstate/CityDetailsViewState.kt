package com.fo.weathercities.presentation.view.viewstate

import com.fo.weathercities.data.model.City

data class CityDetailsViewState(
    val loading: Boolean = false,
    val error: Boolean = false,
    val isCelsius: Boolean = true,
    val city: City? = null
)
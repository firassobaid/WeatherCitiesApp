package com.fo.weathercities.presentation.view.intent

import com.fo.weathercities.data.model.City

sealed class CityDetailsIntent
data class DetailsInit(val city: City) : CityDetailsIntent()
object ConvertInit : CityDetailsIntent()
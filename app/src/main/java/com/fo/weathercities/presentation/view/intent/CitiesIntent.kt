package com.fo.weathercities.presentation.view.intent

import com.fo.weathercities.data.model.City
import java.io.InputStream

sealed class CitiesIntent
data class CitiesInit(val stream: InputStream?) : CitiesIntent()
data class CityClickIntent(val city: City) : CitiesIntent()